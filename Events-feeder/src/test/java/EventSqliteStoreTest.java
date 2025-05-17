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
        String dbUrl = "jdbc:sqlite::memory:";  // Base de datos en memoria para pruebas
        store = new EventSqliteStore(dbUrl);
        conn = DriverManager.getConnection(dbUrl);
    }

    @Test
    void shouldInsertAndRetrieveEvent() throws Exception {
        Event event = new Event(
                "ticketmaster",
                "event-1",
                "Test Event",
                "music",
                List.of("Madrid"),
                "ES",
                Instant.now(),
                "2026-05-30T00:00:00Z",
                "https://example.com/event",
                "40.4168,-3.7038"
        );

        store.saveEvent(event);

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM events WHERE id = ?")) {
            ps.setString(1, "event-1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "El evento debería estar presente en la base de datos");
                assertEquals("Test Event", rs.getString("name"), "El nombre del evento no coincide");
                assertEquals("music", rs.getString("keyword"), "El keyword del evento no coincide");
                assertEquals("Madrid", rs.getString("city"), "La ciudad del evento no coincide");
                assertEquals("ES", rs.getString("country_code"), "El código de país no coincide");
                assertEquals("https://example.com/event", rs.getString("url"), "La URL del evento no coincide");
                assertEquals("40.4168,-3.7038", rs.getString("latlong"), "El latlong del evento no coincide");
            }
        }
    }

    @Test
    void shouldReplaceExistingEvent() throws Exception {
        Event originalEvent = new Event(
                "ticketmaster",
                "event-2",
                "Original Event",
                "music",
                List.of("Barcelona"),
                "ES",
                Instant.now(),
                "2026-05-30T00:00:00Z",
                "https://example.com/original",
                "41.3851,2.1734"
        );
        Event updatedEvent = new Event(
                "ticketmaster",
                "event-2",
                "Updated Event",
                "sports",
                List.of("Barcelona"),
                "ES",
                Instant.now(),
                "2026-06-30T00:00:00Z",
                "https://example.com/updated",
                "41.3851,2.1734"
        );

        store.saveEvent(originalEvent);
        store.saveEvent(updatedEvent);

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM events WHERE id = ?")) {
            ps.setString(1, "event-2");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "El evento actualizado debería estar presente en la base de datos");
                assertEquals("Updated Event", rs.getString("name"), "El nombre del evento no se actualizó correctamente");
                assertEquals("sports", rs.getString("keyword"), "El keyword del evento no se actualizó correctamente");
                assertEquals("2026-06-30T00:00:00Z", rs.getString("date"), "La fecha del evento no se actualizó correctamente");
                assertEquals("https://example.com/updated", rs.getString("url"), "La URL del evento no se actualizó correctamente");
            }
        }
    }

    @AfterAll
    static void tearDown() throws Exception {
        conn.close();
    }
}
