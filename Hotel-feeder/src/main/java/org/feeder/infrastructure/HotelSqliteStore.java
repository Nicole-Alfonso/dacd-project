package org.feeder.infrastructure;

import org.feeder.application.HotelStore;
import org.feeder.model.HotelData;
import org.shared.PriceOffer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelSqliteStore implements HotelStore {

    private final String dbUrl;

    public HotelSqliteStore(String dbUrl) {
        this.dbUrl = dbUrl;
        initDatabase();
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String createHotelTable = """
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
                );
            """;

            String createOfferTable = """
                CREATE TABLE IF NOT EXISTS offers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    hotel_id TEXT,
                    provider TEXT,
                    price REAL,
                    currency TEXT,
                    FOREIGN KEY(hotel_id) REFERENCES hotels(id)
                );
            """;

            conn.createStatement().execute(createHotelTable);
            conn.createStatement().execute(createOfferTable);

        } catch (SQLException e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
        }
    }

    @Override
    public void saveHotel(HotelData hotel) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false); // Mejora rendimiento en lote

            // Guardar hotel
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

            // Guardar ofertas sin duplicar
            String checkOffer = """
                SELECT COUNT(*) FROM offers
                WHERE hotel_id = ? AND provider = ? AND price = ? AND currency = ?;
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
                            // Solo insertamos si no existe
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

            conn.commit(); // Confirmar todos los cambios

        } catch (SQLException e) {
            System.err.println("Error guardando hotel en la base de datos: " + e.getMessage());
        }
    }
}
