package org.eventstore.launcher;

import org.eventstore.core.EventStoreBuilder;

public class BuilderLauncher {
    public static void main(String[] args) {
        System.out.println("Event Store Builder iniciado...");

        EventStoreBuilder builder = new EventStoreBuilder();
        builder.start();

        // Mantener el proceso activo indefinidamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Event Store Builder detenido.");
        }));

        try {
            // Evita que el hilo principal finalice
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Interrupción en BuilderLauncher: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
