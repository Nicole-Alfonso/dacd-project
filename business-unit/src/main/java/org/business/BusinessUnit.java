package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;
import java.util.List;
import java.util.Optional;

public class BusinessUnit {
    private final Datamart datamart;

    public BusinessUnit() {
        this.datamart = new Datamart();
    }

    public void start() {
        EventSubscriber.startListening(datamart);
        HistoricalEventLoader.loadFromFolder("eventstore/HotelPrice/Xotelo", datamart);
    }

    public List<HotelEvent> getHotelesFiltrados(String nombreEvento, String categoria,
                                                double maxPrecio, double minRating, double maxDistanciaKm) {
        return datamart.findEventoByNombre(nombreEvento)
                .map(e -> datamart.getHotelesFiltrados(e.getCity(), e.getLat(), e.getLon(), maxPrecio, categoria, minRating, maxDistanciaKm))
                .orElse(List.of());
    }

    public List<HotelEvent> getHotelesParaEvento(String nombreEvento, String categoria, double maxPrecio, double minRating, double maxDistanciaKm) {
        Optional<EventInfo> eventoOpt = datamart.getEventos().stream()
                .filter(e -> e.getName().equalsIgnoreCase(nombreEvento))
                .findFirst();

        if (eventoOpt.isEmpty()) {
            System.out.println("⚠ Evento no encontrado: " + nombreEvento);
            return List.of();
        }

        EventInfo evento = eventoOpt.get();

        return datamart.getHotelesFiltrados(
                evento.getCity(),
                evento.getLat(),
                evento.getLon(),
                maxPrecio,
                categoria,
                minRating,
                maxDistanciaKm
        );
    }

}
