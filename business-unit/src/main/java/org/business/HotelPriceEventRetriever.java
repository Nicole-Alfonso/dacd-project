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
        EventStoreLoader.loadAllEvents(datamart);
        return datamart.findHotelEvents(city, checkIn, checkOut);
    }
}