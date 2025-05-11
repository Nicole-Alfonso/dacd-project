package org.example.business;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.shared.HotelEvent;

import javax.jms.*;

public class EventSubscriber {

    public static void startListening(Datamart datamart) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = factory.createConnection();
            connection.setClientID("BusinessUnit");
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("HotelPrice");
            MessageConsumer consumer = session.createDurableSubscriber(topic, "BusinessSub");

            Gson gson = new Gson();

            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage text) {
                    try {
                        HotelEvent event = gson.fromJson(text.getText(), HotelEvent.class);
                        datamart.addEvent(event);
                        System.out.println("Evento recibido (realtime): " + event.name);
                    } catch (Exception e) {
                        System.err.println("Error al procesar mensaje: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Error al conectar con ActiveMQ: " + e.getMessage());
        }
    }
}
