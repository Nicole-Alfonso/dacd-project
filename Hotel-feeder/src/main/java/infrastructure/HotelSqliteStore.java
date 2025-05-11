package infrastructure;

import application.HotelStore;
import domain.model.HotelData;
import org.example.shared.PriceOffer;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class HotelSqliteStore implements HotelStore {

    private final String dbUrl;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HotelSqliteStore(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS hotels (" +
                    "id TEXT PRIMARY KEY, name TEXT, address TEXT, province TEXT, " +
                    "rating REAL, latitude REAL, longitude REAL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS price_offers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, hotel_id TEXT, provider TEXT, " +
                    "price REAL, currency TEXT, timestamp TEXT, " +
                    "FOREIGN KEY (hotel_id) REFERENCES hotels(id))");

        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void saveHotel(HotelData hotelData) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);

            String upsert = "INSERT OR REPLACE INTO hotels (id, name, address, province, rating, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(upsert)) {
                stmt.setString(1, hotelData.getId());
                stmt.setString(2, hotelData.getName());
                stmt.setString(3, hotelData.getAddress());
                stmt.setString(4, hotelData.getCity());
                stmt.setDouble(5, hotelData.getRating());
                stmt.setDouble(6, hotelData.getLatitude());
                stmt.setDouble(7, hotelData.getLongitude());
                stmt.executeUpdate();
            }

            String timestamp = hotelData.getTimestamp().format(formatter);
            String insertOffer = "INSERT INTO price_offers (hotel_id, provider, price, currency, timestamp) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(insertOffer)) {
                for (PriceOffer offer : hotelData.getPriceOffers()) {
                    stmt.setString(1, hotelData.getId());
                    stmt.setString(2, offer.getProvider());
                    stmt.setDouble(3, offer.getPrice());
                    stmt.setString(4, offer.getCurrency());
                    stmt.setString(5, timestamp);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error saving hotel: " + e.getMessage());
        }
    }
}
