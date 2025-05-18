package org.business;

import org.shared.HotelEvent;
import org.business.events.EventStoreLoader;
import java.time.LocalDate;
import java.util.List;

public class HotelPriceEventRetriever {

    private final Datamart datamart;

    public HotelPriceEventRetriever(Datamart datamart) {
        this.datamart = datamart;
    }

    public List<HotelEvent> retrieveAndStore(String city, LocalDate checkIn, LocalDate checkOut, Datamart datamart) {
        // Vuelve a cargar los eventos de hotel desde disco (eventstore/)
        EventStoreLoader.loadAllEvents(datamart);

        // Devuelve los hoteles ya cargados en memoria que coincidan
        return datamart.findHotelEvents(city, checkIn, checkOut);
    }
}
