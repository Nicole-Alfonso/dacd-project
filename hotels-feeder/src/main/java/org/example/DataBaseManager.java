package org.example;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataBaseManager {
    private final String dbUrl;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DataBaseManager(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            // Crear tabla de hoteles
            String createHotelsTable = "CREATE TABLE IF NOT EXISTS hotels (" +
                    "id TEXT PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT, " +
                    "province TEXT" +
                    ")";
            stmt.execute(createHotelsTable);

            // Crear tabla de ofertas de precios
            String createPriceOffersTable = "CREATE TABLE IF NOT EXISTS price_offers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "hotel_id TEXT NOT NULL, " +
                    "provider TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "currency TEXT NOT NULL, " +
                    "timestamp TEXT NOT NULL, " +
                    "FOREIGN KEY (hotel_id) REFERENCES hotels (id)" +
                    ")";
            stmt.execute(createPriceOffersTable);

            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public void saveHotelData(HotelData hotelData) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);

            // Insertar o actualizar información del hotel
            String upsertHotel = "INSERT OR REPLACE INTO hotels (id, name, address, province) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(upsertHotel)) {
                pstmt.setString(1, hotelData.getId());
                pstmt.setString(2, hotelData.getName());
                pstmt.setString(3, hotelData.getAddress());
                pstmt.setString(4, hotelData.getCity());
                pstmt.executeUpdate();
            }

            // Para cada oferta de precio, comprobar si ya existe una igual en la base de datos
            List<PriceOffer> offers = hotelData.getPriceOffers();
            String timestamp = hotelData.getTimestamp().format(formatter);

            String checkExistingOffer = "SELECT price FROM price_offers " +
                    "WHERE hotel_id = ? AND provider = ? " +
                    "ORDER BY timestamp DESC LIMIT 1";

            String insertPriceOffer = "INSERT INTO price_offers (hotel_id, provider, price, currency, timestamp) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkExistingOffer);
                 PreparedStatement insertStmt = conn.prepareStatement(insertPriceOffer)) {

                for (PriceOffer offer : offers) {
                    // Comprobar si existe una oferta reciente con precio diferente
                    checkStmt.setString(1, hotelData.getId());
                    checkStmt.setString(2, offer.getProvider());

                    boolean shouldInsert = true;

                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            double lastPrice = rs.getDouble("price");
                            // Solo insertar si el precio ha cambiado
                            if (Math.abs(lastPrice - offer.getPrice()) < 0.01) {
                                shouldInsert = false;
                            }
                        }
                    }

                    // Si es un precio nuevo o ha cambiado, insertarlo
                    if (shouldInsert) {
                        insertStmt.setString(1, hotelData.getId());
                        insertStmt.setString(2, offer.getProvider());
                        insertStmt.setDouble(3, offer.getPrice());
                        insertStmt.setString(4, offer.getCurrency());
                        insertStmt.setString(5, timestamp);
                        insertStmt.executeUpdate();
                        System.out.println("Inserted new price for hotel " + hotelData.getName() +
                                " from provider " + offer.getProvider() + ": " + offer.getPrice() + " " + offer.getCurrency());
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error saving hotel data: " + e.getMessage());
        }
    }

    public List<HotelData> getHotelsByProvince(String province, double minPrice, double maxPrice) {
        // Implementar método para obtener hoteles filtrados por provincia y rango de precios
        // Esta es una consulta más compleja que involucraría unir las tablas hotels y price_offers
        // Devolvería una lista de HotelData con sus respectivas ofertas de precios
        // No implementado en esta versión básica
        return null;
    }
}