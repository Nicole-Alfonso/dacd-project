package org.api.dto;

import org.shared.HotelFilter;

import java.time.LocalDate;

public class HotelFilterRequest {
    private String eventName;
    private String city;
    private String category;
    private double maxPrice;
    private double minRating;
    private double maxDistanceKm;
    private LocalDate checkIn;
    private int nights;

    public HotelFilterRequest(String eventName, String city, LocalDate checkIn, int nights,
                              String category, double maxPrice, double minRating, double maxDistanceKm) {
        this.eventName = eventName;
        this.city = city;
        this.checkIn = checkIn;
        this.nights = nights;
        this.category = category;
        this.maxPrice = maxPrice;
        this.minRating = minRating;
        this.maxDistanceKm = maxDistanceKm;
    }

    public HotelFilter toFilter() { return new HotelFilter(category, maxPrice, minRating, maxDistanceKm); }

    public String getEventName() { return eventName; }
    public String getCity() { return city; }
    public String getCategory() { return category; }
    public double getMaxPrice() { return maxPrice; }
    public double getMinRating() { return minRating; }
    public double getMaxDistanceKm() { return maxDistanceKm; }
    public LocalDate getCheckIn() { return checkIn; }
    public int getNights() { return nights; }
}
