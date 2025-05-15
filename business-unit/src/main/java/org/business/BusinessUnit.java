package org.business;

import org.shared.HotelEvent;

import java.util.List;

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
        return datamart.getEventos().stream()
                .filter(e -> e.name.toLowerCase().contains(eventName.toLowerCase()))
                .findFirst()
                .map(e -> datamart.getHotelsUnderPrice(e.city, maxPrice))
                .orElse(List.of());
    }
}


