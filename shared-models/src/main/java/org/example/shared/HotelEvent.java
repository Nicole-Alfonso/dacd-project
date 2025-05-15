package org.example.shared;

import java.time.Instant;
import java.util.List;

public class HotelEvent {
    public Instant ts;       // Timestamp UTC
    public String ss;        // Source system (e.g. "Xotelo")
    public String id;
    public String name;
    public String city;
    public double rating;
    public double lat;
    public double lon;
    public double minPrice;
    public double maxPrice;
    public String category;
    public List<PriceOffer> priceOffers;

    public HotelEvent() {}

    public HotelEvent(String ss, String id, String name, String city,
                      double rating, double lat, double lon,
                      double minPrice, double maxPrice, String category,
                      List<PriceOffer> priceOffers) {
        this.ts = Instant.now();
        this.ss = ss;
        this.id = id;
        this.name = name;
        this.city = city;
        this.rating = rating;
        this.lat = lat;
        this.lon = lon;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.category = category;
        this.priceOffers = priceOffers;
    }

    @Override
    public String toString() {
        return "[HotelEvent] " + name + " (" + city + ") - " + category + ": $" + minPrice;
    }
}
