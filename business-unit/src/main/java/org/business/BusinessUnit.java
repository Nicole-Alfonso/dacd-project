package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BusinessUnit {

    private final Datamart datamart;
    private final HotelPriceEventRetriever hotelRetriever;

    public BusinessUnit(Datamart datamart) {
        this.datamart = datamart;
        this.hotelRetriever = new HotelPriceEventRetriever(datamart);
    }

    public List<HotelEvent> getHotelsNearEvent(String concertId, LocalDate eventDate, int nights, double minRating) {
        Optional<EventInfo> concertOpt = datamart.findEventById(concertId);
        if (concertOpt.isEmpty()) return List.of();

        EventInfo concert = concertOpt.get();
        String city = concert.getCity();
        LocalDate checkIn = eventDate;
        LocalDate checkOut = checkIn.plusDays(nights);

        List<HotelEvent> hotelEvents = datamart.findHotelEvents(city, checkIn, checkOut);

        if (hotelEvents.isEmpty()) {
            System.out.println("Buscando hoteles dinámicamente...");
            hotelEvents = hotelRetriever.retrieveAndStore(city, checkIn, checkOut);
        }

        return hotelEvents.stream()
                .filter(h -> h.getRating() >= minRating)
                .collect(Collectors.toList());
    }
}
