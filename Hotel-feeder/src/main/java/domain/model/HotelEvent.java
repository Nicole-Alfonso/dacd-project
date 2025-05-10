package domain.model;

import java.time.Instant;
import java.util.List;

public class HotelEvent {
    public Instant ts; // Timestamp UTC
    public String ss;  // Source: Xotelo
    public String id, name, address, city, province;
    public double rating, lat, lon;
    public List<PriceOffer> priceOffers;

    public HotelEvent(String ss, HotelData data) {
        this.ts = Instant.now();
        this.ss = ss;
        this.id = data.getId();
        this.name = data.getName();
        this.address = data.getAddress();
        this.city = data.getCity();
        this.rating = data.getRating();
        this.lat = data.getLatitude();
        this.lon = data.getLongitude();
        this.priceOffers = data.getPriceOffers();
    }
}