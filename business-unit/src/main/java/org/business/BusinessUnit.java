package org.business;

import org.shared.HotelEvent;
import org.shared.EventInfo;

import java.util.List;
import java.util.Optional;

public class BusinessUnit {
    private final Datamart datamart = new Datamart();

    public void start() {
        EventSubscriber.startListening(datamart);
        HistoricalEventLoader.loadFromFolder("eventstore/HotelPrice/Xotelo", datamart);
    }

    public List<HotelEvent> getHotelesParaEvento(String nombreEvento, double maxPrecio, String categoria, double minRating, double maxDistanciaKm) {
        Optional<EventInfo> eventoOpt = datamart.getEventos().stream()
                .filter(e -> e.name.equalsIgnoreCase(nombreEvento))
                .findFirst();

        if (eventoOpt.isEmpty()) {
            System.out.println("⚠ Evento no encontrado: " + nombreEvento);
            return List.of();
        }

        EventInfo evento = eventoOpt.get();
        return datamart.getHotelesFiltrados(evento.city, evento.lat, evento.lon,
                maxPrecio, categoria, minRating, maxDistanciaKm);
    }
}
