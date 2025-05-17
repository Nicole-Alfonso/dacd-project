package org.feeder.model;

import org.shared.PriceOffer;
import java.time.LocalDateTime;
import java.util.List;

public class HotelData {

    public enum PriceCategory { LOW, MEDIUM, HIGH }

    private final String id;
    private final String name;
    private final String city;
    private final double rating;
    private final double latitude;
    private final double longitude;
    private final List<PriceOffer> priceOffers;
    private final double minPrice;
    private final double maxPrice;
    private final PriceCategory category;
    private final LocalDateTime timestamp;

    public HotelData(String id, String name, String city, double rating,
                     double latitude, double longitude, List<PriceOffer> priceOffers) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.priceOffers = priceOffers;
        this.timestamp = LocalDateTime.now();

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
    public double getRating() { return rating; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public List<PriceOffer> getPriceOffers() { return priceOffers; }
    public double getMinPrice() { return minPrice; }
    public double getMaxPrice() { return maxPrice; }
    public PriceCategory getCategory() { return category; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public String toString() {
        return String.format("%s [%s] %.1f⭐ (%s) %.2f - %.2f\n%s",
                name, city, rating, category, minPrice, maxPrice,
                priceOffers.stream().map(PriceOffer::toString).reduce("", (a, b) -> a + " - " + b + "\n"));
    }
}