package org.feeder.infrastructure;

import org.feeder.application.HotelStore;
import org.feeder.model.HotelData;
import org.shared.PriceOffer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HotelSqliteStore implements HotelStore {

    private final String dbUrl;

    public HotelSqliteStore(String dbUrl) {
        this.dbUrl = dbUrl;
        initDatabase();
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            executeUpdate(conn, """
                CREATE TABLE IF NOT EXISTS hotels (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    city TEXT,
                    rating REAL,
                    latitude REAL,
                    longitude REAL,
                    min_price REAL,
                    max_price REAL,
                    category TEXT
                );""");

            executeUpdate(conn, """
                CREATE TABLE IF NOT EXISTS offers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    hotel_id TEXT,
                    provider TEXT,
                    price REAL,
                    currency TEXT,
                    FOREIGN KEY(hotel_id) REFERENCES hotels(id)
                );""");

        } catch (SQLException e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
        }
    }

    private void executeUpdate(Connection conn, String sql) throws SQLException {
        try (var stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public void saveHotel(HotelData hotel) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String insertHotel = """
                INSERT OR REPLACE INTO hotels (id, name, city, rating, latitude, longitude, min_price, max_price, category)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

            try (PreparedStatement stmt = conn.prepareStatement(insertHotel)) {
                stmt.setString(1, hotel.getId());
                stmt.setString(2, hotel.getName());
                stmt.setString(3, hotel.getCity());
                stmt.setDouble(4, hotel.getRating());
                stmt.setDouble(5, hotel.getLatitude());
                stmt.setDouble(6, hotel.getLongitude());
                stmt.setDouble(7, hotel.getMinPrice());
                stmt.setDouble(8, hotel.getMaxPrice());
                stmt.setString(9, hotel.getCategory().name());
                stmt.executeUpdate();
            }

            String insertOffer = """
                INSERT INTO offers (hotel_id, provider, price, currency)
                VALUES (?, ?, ?, ?);
                """;

            for (PriceOffer offer : hotel.getPriceOffers()) {
                try (PreparedStatement stmt = conn.prepareStatement(insertOffer)) {
                    stmt.setString(1, hotel.getId());
                    stmt.setString(2, offer.getProvider());
                    stmt.setDouble(3, offer.getPrice());
                    stmt.setString(4, offer.getCurrency());
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Error guardando hotel en la base de datos: " + e.getMessage());
        }
    }
}