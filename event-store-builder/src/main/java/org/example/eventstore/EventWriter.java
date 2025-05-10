package org.example.eventstore;

public interface EventWriter {
    void write(String topic, String jsonEvent);
}