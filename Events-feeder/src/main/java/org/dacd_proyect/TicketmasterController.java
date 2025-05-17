package org.dacd_proyect;

import org.dacd_proyect.application.EventProvider;
import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.domain.model.Event;
import org.shared.InstantTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.Instant;
import java.util.List;

public class TicketmasterController {
    private final EventProvider provider;
    private final EventStore store;

    public TicketmasterController(EventProvider provider, EventStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void fetchSaveAndPublish(List<String> cities, String startDateTime) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        for (String city : cities) {
            List<Event> events = provider.fetchEvents(city, startDateTime);

            Connection connection = null;
            Session session = null;

            try {
                connection = factory.createConnection();
                connection.start();

                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createTopic("TicketmasterEvents");
                MessageProducer producer = session.createProducer(destination);

                for (Event event : events) {
                    store.saveEvent(event);
                    String json = gson.toJson(event);
                    TextMessage message = session.createTextMessage(json);
                    producer.send(message);
                }

                System.out.println("Eventos enviados a ActiveMQ para la ciudad: " + city);

            } catch (JMSException e) {
                System.err.println("Error enviando evento a ActiveMQ: " + e.getMessage());

            } finally {
                if (session != null) {
                    try {
                        session.close();
                    } catch (JMSException e) {
                        System.err.println("Error cerrando session: " + e.getMessage());
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException e) {
                        System.err.println("Error cerrando connection: " + e.getMessage());
                    }
                }
            }
        }
    }
}




