package org.eventstore.listener;

import org.eventstore.writer.EventWriter;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class GenericEventListener implements MessageListener {

    private final String topic;
    private final EventWriter writer;

    public GenericEventListener(String topic, EventWriter writer) {
        this.topic = topic;
        this.writer = writer;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String json = ((TextMessage) message).getText();
                writer.write(topic, json);
            } catch (Exception e) {
                System.err.println("Error procesando mensaje: " + e.getMessage());
            }
        }
    }
}
