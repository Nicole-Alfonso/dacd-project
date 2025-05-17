import org.dacd_proyect.domain.model.Event;
import org.dacd_proyect.infrastructure.EventSqliteStore;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventSqliteStoreTest {

    private static Connection conn;
    private static EventSqliteStore store;

    @BeforeAll
    static void setup() throws Exception {
        String dbUrl = "jdbc:sqlite::memory:";  // In-memory database
        store = new EventSqliteStore(dbUrl);
        conn = DriverManager.getConnection(dbUrl);
    }

    @Test
    void shouldInsertEvent() throws Exception {
        Event event = new Event(
                "ticketmaster",
                "event123",
                "Test Event",
                "Music",
                List.of("Madrid"),
                "ES",
                Instant.now(),
                "2026-05-30T00:00:00Z",
                "https://example.com",
                "40.4168,-3.7038"
        );

        store.saveEvent(event);

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM events WHERE id = ?")) {
            ps.setString(1, "event123");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Test Event", rs.getString("name"));
                assertEquals("ticketmaster", rs.getString("ss"));
                assertEquals("ES", rs.getString("country_code"));
            }
        }
    }

    @AfterAll
    static void tearDown() throws Exception {
        conn.close();
    }
}

