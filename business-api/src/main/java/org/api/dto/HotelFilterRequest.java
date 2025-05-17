package org.api.dto;

public class HotelFilterRequest {
    private String eventName;
    private String category;
    private double maxPrice;
    private double minRating;
    private double maxDistanceKm;

    public HotelFilterRequest() {}

    public HotelFilterRequest(String eventName, String category, double maxPrice, double minRating, double maxDistanceKm) {
        this.eventName = eventName;
        this.category = category;
        this.maxPrice = maxPrice;
        this.minRating = minRating;
        this.maxDistanceKm = maxDistanceKm;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCategory() {
        return category;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public double getMinRating() {
        return minRating;
    }

    public double getMaxDistanceKm() {
        return maxDistanceKm;
    }

    // setters opcionales si quieres usar @RequestBody directamente
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }

    public void setMaxDistanceKm(double maxDistanceKm) {
        this.maxDistanceKm = maxDistanceKm;
    }
}
