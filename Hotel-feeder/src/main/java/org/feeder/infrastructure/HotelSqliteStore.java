package org.feeder.infrastructure;

import org.feeder.application.HotelStore;
import org.feeder.model.HotelData;
import org.shared.PriceOffer;

import java.sql.*;

public class HotelSqliteStore implements HotelStore {

    private final String dbUrl;

    public HotelSqliteStore(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS hotels (
                    id TEXT PRIMARY KEY,
                    city_code TEXT,
                    city TEXT,
                    name TEXT,
                    rating REAL,
                    latitude REAL,
                    longitude REAL,
                    min_price REAL,
                    max_price REAL,
                    category TEXT
                    url TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS offers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    hotel_id TEXT,
                    provider TEXT,
                    price REAL,
                    currency TEXT,
                    FOREIGN KEY(hotel_id) REFERENCES hotels(id)
                );
            """);

        } catch (SQLException e) {
            System.err.println("Error inicializando la base de datos: " + e.getMessage());
        }
    }

    @Override
    public void saveHotel(HotelData hotel) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);

            insertOrUpdateHotel(conn, hotel);
            insertOffersIfNotExists(conn, hotel);

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Error guardando hotel: " + e.getMessage());
        }
    }

    private void insertOrUpdateHotel(Connection conn, HotelData hotel) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("""
            INSERT OR REPLACE INTO hotels (id, city_code, city, name, rating, latitude, longitude, min_price, max_price, category, url)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """)) {
            stmt.setString(1, hotel.getId());
            stmt.setString(2, hotel.getCity_code());
            stmt.setString(3, hotel.getCity());
            stmt.setString(4, hotel.getName());
            stmt.setDouble(5, hotel.getRating());
            stmt.setDouble(6, hotel.getLatitude());
            stmt.setDouble(7, hotel.getLongitude());
            stmt.setDouble(8, hotel.getMinPrice());
            stmt.setDouble(9, hotel.getMaxPrice());
            stmt.setString(10, hotel.getCategory().name());
            stmt.setString(11, hotel.getUrl());
            stmt.executeUpdate();
        }
    }

    private void insertOffersIfNotExists(Connection conn, HotelData hotel) throws SQLException {
        String checkOffer = """
            SELECT COUNT(*) FROM offers WHERE hotel_id = ? AND provider = ? AND price = ? AND currency = ?;
        """;

        String insertOffer = """
            INSERT INTO offers (hotel_id, provider, price, currency)
            VALUES (?, ?, ?, ?);
        """;

        for (PriceOffer offer : hotel.getPriceOffers()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkOffer)) {
                checkStmt.setString(1, hotel.getId());
                checkStmt.setString(2, offer.getProvider());
                checkStmt.setDouble(3, offer.getPrice());
                checkStmt.setString(4, offer.getCurrency());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertOffer)) {
                            insertStmt.setString(1, hotel.getId());
                            insertStmt.setString(2, offer.getProvider());
                            insertStmt.setDouble(3, offer.getPrice());
                            insertStmt.setString(4, offer.getCurrency());
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
        }
    }
}
