package org.example.shared;

import java.time.Instant;
import java.util.List;

public class HotelEvent {
    public Instant ts;       // Timestamp UTC
    public String ss;        // Source (ej: "Xotelo")
    public String id;
    public String name;
    public String address;
    public String city;
    public String province;
    public double rating;
    public double lat;
    public double lon;
    public List<PriceOffer> priceOffers;

    // Constructor estándar (necesario para Gson)
    public HotelEvent() {}

    // Constructor manual (si lo usas desde feeder)
    public HotelEvent(String ss, String id, String name, String address, String city, String province,
                      double rating, double lat, double lon, List<PriceOffer> priceOffers) {
        this.ts = Instant.now();
        this.ss = ss;
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.rating = rating;
        this.lat = lat;
        this.lon = lon;
        this.priceOffers = priceOffers;
    }
}