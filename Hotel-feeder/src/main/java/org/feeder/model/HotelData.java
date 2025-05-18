package org.feeder.model;

import org.shared.PriceOffer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class HotelData {

    public enum PriceCategory { LOW, MEDIUM, HIGH }

    private final String id;
    private final String city_code;
    private final String city;
    private final String name;
    private final double rating;
    private final double latitude;
    private final double longitude;
    private final List<PriceOffer> priceOffers;
    private final double minPrice;
    private final double maxPrice;
    private final PriceCategory category;
    private final Instant timestamp;
    private final String url;

    public HotelData(String id, String city_code, String city, String name, double rating,
                     double latitude, double longitude, List<PriceOffer> priceOffers, Instant timestamp, String url) {
        this.id = id;
        this.city_code = city_code;
        this.city = city;
        this.name = name;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.priceOffers = priceOffers;
        this.timestamp = timestamp;
        this.url = url;

        this.minPrice = priceOffers.stream().mapToDouble(PriceOffer::getPrice).min().orElse(0);
        this.maxPrice = priceOffers.stream().mapToDouble(PriceOffer::getPrice).max().orElse(0);

        if (minPrice < 80) {
            this.category = PriceCategory.LOW;
        } else if (minPrice < 150) {
            this.category = PriceCategory.MEDIUM;
        } else {
            this.category = PriceCategory.HIGH;
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCity_code() { return city_code; }
    public double getRating() { return rating; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public List<PriceOffer> getPriceOffers() { return priceOffers; }
    public double getMinPrice() { return minPrice; }
    public double getMaxPrice() { return maxPrice; }
    public PriceCategory getCategory() { return category; }
    public Instant getTimestamp() { return timestamp; }
    public String getUrl() { return url; }

    public String toString() {
        return String.format("%s [%s] %.1f⭐ (%s) %.2f - %.2f\n%s",
                name, city, rating, category, minPrice, maxPrice,
                priceOffers.stream().map(PriceOffer::toString).reduce("", (a, b) -> a + " - " + b + "\n"));
    }
}