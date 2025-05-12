package org.dacd_proyect;

import org.dacd_proyect.application.EventProvider;
import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.domain.model.Event;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.List;
import javax.jms.*;

public class TicketmasterController {
    private final EventProvider provider;
    private final EventStore store;

    public TicketmasterController(EventProvider provider, EventStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void fetchSaveAndPublish(String location, String date) {
        List<Event> events = provider.fetchEvents(location, date);
        Gson gson = new Gson();

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("TicketmasterEvents");
            MessageProducer producer = session.createProducer(destination);

            for (Event event : events) {
                store.saveEvent(event);

                String json = gson.toJson(event);
                TextMessage message = session.createTextMessage(json);
                producer.send(message);
            }

            session.close();
            connection.close();

        } catch (JMSException e) {
            System.err.println("Error enviando evento a ActiveMQ: " + e.getMessage());
        }
    }
}






