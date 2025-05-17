package org.business;

import org.shared.HotelEvent;
import java.util.List;

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
}
