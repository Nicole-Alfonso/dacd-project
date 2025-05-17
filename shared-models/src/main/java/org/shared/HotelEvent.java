package org.shared;

import java.time.Instant;
import java.util.List;

public class HotelEvent {
    private final Instant ts;
    private final String ss;
    private final String id;
    private final String name;
    private final String city;
    private final double rating;
    private final double lat;
    private final double lon;
    private final double minPrice;
    private final double maxPrice;
    private final String category;
    private final List<PriceOffer> priceOffers;

    public HotelEvent(Instant ts, String ss, String id, String name, String city,
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

    // Getters
    public Instant getTs() { return ts; }
    public String getSs() { return ss; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public double getRating() { return rating; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public double getMinPrice() { return minPrice; }
    public double getMaxPrice() { return maxPrice; }
    public String getCategory() { return category; }
    public List<PriceOffer> getPriceOffers() { return priceOffers; }

    @Override
    public String toString() {
        return "[HotelEvent] " + name + " (" + city + ") - " + category + ": $" + minPrice;
    }
}
