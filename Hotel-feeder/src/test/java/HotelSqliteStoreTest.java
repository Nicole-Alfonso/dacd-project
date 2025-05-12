import domain.model.HotelData;
import org.example.shared.PriceOffer;
import org.junit.jupiter.api.Test;
import infrastructure.HotelSqliteStore;

import java.sql.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelSqliteStoreTest {

    @Test
    void testSaveHotelCreatesTablesAndInsertsData() {
        String inMemoryDb = "jdbc:sqlite::memory:";
        HotelSqliteStore store = new HotelSqliteStore(inMemoryDb);

        List<PriceOffer> offers = List.of(new PriceOffer("Xotelo", 79.99, "EUR"));
        HotelData hotel = new HotelData("H999", "Hotel Test", "Sevilla", "Sevilla", "Av. de prueba",
                4.5, 37.0, -5.9, offers);

        store.saveHotel(hotel);

        try (Connection conn = DriverManager.getConnection(inMemoryDb);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hotels WHERE id = ?")) {
            stmt.setString(1, "H999");
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals("Hotel Test", rs.getString("name"));

        } catch (SQLException e) {
            fail("No se pudo consultar la base de datos: " + e.getMessage());
        }
    }
}
