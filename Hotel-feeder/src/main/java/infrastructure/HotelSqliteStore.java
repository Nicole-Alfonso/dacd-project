package infrastructure;

import application.HotelStore;
import domain.model.HotelData;
import org.example.shared.PriceOffer;
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
            String createHotelTable = """
                CREATE TABLE IF NOT EXISTS hotels (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    city TEXT,
                    rating REAL,
                    latitude REAL,
                    longitude REAL
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
            // Insertar hotel
            String insertHotel = """
                INSERT OR REPLACE INTO hotels (id, name, city, rating, latitude, longitude)
                VALUES (?, ?, ?, ?, ?, ?);
                """;

            try (PreparedStatement stmt = conn.prepareStatement(insertHotel)) {
                stmt.setString(1, hotel.getId());
                stmt.setString(2, hotel.getName());
                stmt.setString(3, hotel.getCity());
                stmt.setDouble(4, hotel.getRating());
                stmt.setDouble(5, hotel.getLatitude());
                stmt.setDouble(6, hotel.getLongitude());
                stmt.executeUpdate();
            }

            // Insertar ofertas
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
