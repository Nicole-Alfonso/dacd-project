package org.eventstore;

public interface EventWriter {
    void write(String topic, String jsonEvent);
}