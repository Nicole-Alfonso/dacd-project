package org.business;

import org.example.shared.HotelEvent;

import java.util.List;

public class BusinessUnit {
    private final Datamart datamart = new Datamart();

    public void start() {
        EventSubscriber.startListening(datamart);
        HistoricalEventLoader.loadFromFolder("eventstore/HotelPrice/Xotelo", datamart);
    }

    public List<HotelEvent> getBaratos(String provincia, double precioMax) {
        return datamart.getHotelsUnderPrice(provincia, precioMax);
    }

    public List<HotelEvent> getTopValorados(String provincia, int topN) {
        return datamart.getTopRated(provincia, topN);
    }
}
