package org.eventstore.core;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eventstore.listener.GenericEventListener;
import org.eventstore.writer.EventWriter;
import org.eventstore.writer.FileEventWriter;

import javax.jms.*;

public class EventStoreBuilder {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String CLIENT_ID = "EventStoreBuilder";
    private static final String[] TOPICS = {"HotelPrice", "TicketmasterEvents"};

    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.setClientID(CLIENT_ID);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            EventWriter writer = new FileEventWriter();

            for (String topicName : TOPICS) {
                Topic topic = session.createTopic(topicName);
                MessageConsumer consumer = session.createDurableSubscriber(topic, topicName + "-Sub");

                consumer.setMessageListener(new GenericEventListener(topicName, writer));
                System.out.println("Escuchando topic: " + topicName);
            }

        } catch (JMSException e) {
            System.err.println("Error al conectar con ActiveMQ: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al iniciar EventStoreBuilder: " + e.getMessage());
        }
    }
}
