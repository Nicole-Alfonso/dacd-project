package org.dacd_proyect;

import org.dacd_proyect.application.EventProvider;
import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.domain.model.Event;

import java.util.List;

public class TicketmasterController {
    private final EventProvider provider;
    private final EventStore store;

    public TicketmasterController(EventProvider provider, EventStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void run(String location, String date) {
        try {
            // Fetch events from Ticketmaster
            List<Event> events = provider.fetchEvents(location, date);

            // Only save non-duplicate events
            List<Event> existingEvents = store.getAllEvents();
            for (Event event : events) {
                boolean isDuplicate = existingEvents.stream()
                        .anyMatch(e -> e.getId().equals(event.getId()));

                if (!isDuplicate) {
                    store.saveEvents(List.of(event));
                    System.out.println("Evento guardado: " + event.getName() + " en " + event.getLocation());
                } else {
                    System.out.println("Evento duplicado (no guardado): " + event.getName() + " en " + event.getLocation());
                }
            }

            System.out.println("Eventos procesados correctamente.");

        } catch (Exception e) {
            System.err.println("Error al procesar eventos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
