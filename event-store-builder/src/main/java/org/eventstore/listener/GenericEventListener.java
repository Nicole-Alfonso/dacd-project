package org.eventstore.listener;

import org.eventstore.writer.EventWriter;

import javax.jms.*;

public class GenericEventListener implements MessageListener {

    private final String topic;
    private final EventWriter writer;

    public GenericEventListener(String topic, EventWriter writer) {
        this.topic = topic;
        this.writer = writer;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage textMessage) {
            try {
                String json = textMessage.getText();
                writer.write(topic, json);
            } catch (Exception e) {
                System.err.println("Error procesando mensaje del topic [" + topic + "]: " + e.getMessage());
            }
        }
    }
}
