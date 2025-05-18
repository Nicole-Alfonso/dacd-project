import org.feeder.infrastructure.HotelSqliteStore;
import org.feeder.model.HotelData;
import org.junit.jupiter.api.*;
import org.shared.PriceOffer;

import java.sql.*;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HotelSqliteStoreTest {

    private static Connection conn;
    private static HotelSqliteStore store;

    @BeforeAll
    static void setup() throws Exception {
        String dbUrl = "jdbc:sqlite::memory:";  // En memoria
        store = new HotelSqliteStore(dbUrl);
        conn = DriverManager.getConnection(dbUrl);
    }

    @Test
    void shouldInsertHotelAndOffers() throws Exception {
        HotelData hotel = new HotelData(
                "test-id",
                "Test Hotel",
                "TestCity",
                4.5,
                12.34,
                56.78,
                List.of(
                        new PriceOffer("ProviderA", 90.0, "EUR"),
                        new PriceOffer("ProviderB", 120.0, "EUR")
                ), Instant.now(), ""
        );

        store.saveHotel(hotel);

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM hotels WHERE id = ?")) {
            ps.setString(1, "test-id");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Test Hotel", rs.getString("name"));
            }
        }

        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM offers WHERE hotel_id = ?")) {
            ps.setString(1, "test-id");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(2, rs.getInt(1));
            }
        }
    }

    @AfterAll
    static void tearDown() throws Exception {
        conn.close();
    }
}
