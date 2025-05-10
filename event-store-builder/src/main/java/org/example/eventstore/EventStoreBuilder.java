package org.example.eventstore;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class EventStoreBuilder {

    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = factory.createConnection();
            connection.setClientID("EventStoreBuilder");
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Añade aquí los topics que quieres escuchar
            String[] topics = {"HotelPrice", "Weather"};

            EventWriter writer = new FileEventWriter();

            for (String topicName : topics) {
                Topic topic = session.createTopic(topicName);
                MessageConsumer consumer = session.createDurableSubscriber(topic, topicName + "-Sub");

                consumer.setMessageListener(new GenericEventListener(topicName, writer));
                System.out.println("Escuchando topic: " + topicName);
            }

        } catch (Exception e) {
            System.err.println("Error al conectar con ActiveMQ: " + e.getMessage());
        }
    }
}
