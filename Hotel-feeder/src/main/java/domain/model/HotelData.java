package domain.model;

import org.example.shared.PriceOffer;

import java.time.LocalDateTime;
import java.util.List;

public class HotelData {
    private final String id;
    private final String name;
    private final String city;
    private final String province;
    private final String address;
    private final double rating;
    private final double latitude;
    private final double longitude;
    private final List<PriceOffer> priceOffers;
    private final LocalDateTime timestamp;

    public HotelData(String id, String name, String city, String province, String address, double rating, double latitude, double longitude, List<PriceOffer> priceOffers) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.province = province;
        this.address = address;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.priceOffers = priceOffers;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public String getAddress() { return address; }
    public double getRating() { return rating; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public List<PriceOffer> getPriceOffers() {return priceOffers; }
    public LocalDateTime getTimestamp() {return timestamp; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hotel: ").append(name).append("\n");
        sb.append("Address: ").append(address).append("\n");
        sb.append("City: ").append(city).append("\n");
        sb.append("Price Offers:\n");

        for (PriceOffer offer : priceOffers) {
            sb.append("  - ").append(offer.toString()).append("\n");
        }

        return sb.toString();
    }

}
