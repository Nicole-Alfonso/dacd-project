package org.example.eventstore;

public class BuilderLauncher {
    public static void main(String[] args) {
        EventStoreBuilder builder = new EventStoreBuilder();
        builder.start();

        // Mantiene el proceso vivo
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
