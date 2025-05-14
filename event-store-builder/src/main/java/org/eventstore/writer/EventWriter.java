package org.eventstore.writer;

public interface EventWriter {
    void write(String topic, String jsonEvent);
}