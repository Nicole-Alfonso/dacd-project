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

    public List<HotelEvent> getBaratos(String ciudad, double precioMax) {
        return datamart.getHotelsUnderPrice(ciudad, precioMax);
    }

    public List<HotelEvent> getTopValorados(String ciudad, int topN) {
        return datamart.getTopRated(ciudad, topN);
    }

    public List<HotelEvent> getHotelesPorCategoria(String ciudad, String categoria) {
        return datamart.getHotelsByCategory(ciudad, categoria);
    }

    public List<HotelEvent> getHotelesParaEvento(String eventName, double maxPrice) {
        Optional<EventInfo> evento = datamart.getEventos().stream()
                .filter(e -> e.name.equalsIgnoreCase(eventName))
                .findFirst();

        if (evento.isEmpty()) {
            System.out.println("Evento no encontrado: " + eventName);
            return List.of();
        }

        String ciudad = evento.get().city;
        return datamart.getHotelsUnderPrice(ciudad, maxPrice);
    }

    public List<HotelEvent> getHotelesFiltradosParaEvento(
            String eventName,
            Optional<String> categoria,
            Optional<Double> maxPrecio,
            Optional<Double> minRating,
            Optional<Double> maxDistKm
    ) {
        return datamart.buscarHotelesParaEvento(eventName, categoria, maxPrecio, minRating, maxDistKm);
    }

}
