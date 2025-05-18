package org.business;

import org.feeder.XoteloController;
import org.feeder.application.HotelProvider;
import org.feeder.application.HotelStore;
import org.feeder.infrastructure.XoteloProvider;
import org.feeder.infrastructure.HotelSqliteStore;
import org.shared.HotelEvent;

import java.time.LocalDate;
import java.util.List;

public class HotelPriceEventRetriever {

    private final HotelProvider provider;
    private final HotelStore store;
    private final Datamart datamart;

    public HotelPriceEventRetriever(Datamart datamart) {
        this.provider = new XoteloProvider();
        this.store = new HotelSqliteStore("jdbc:sqlite:hotels.db");
        this.datamart = datamart;
    }

    public List<HotelEvent> retrieveAndStore(String cityName, LocalDate checkIn, LocalDate checkOut) {
        XoteloController controller = new XoteloController(provider, store);
        controller.fetchSaveAndPublish(cityName, cityName, checkIn, checkOut);

        org.business.events.EventStoreLoader.loadAllEvents(datamart);


        return datamart.findHotelEvents(cityName, checkIn, checkOut);
    }
}
