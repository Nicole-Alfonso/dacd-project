
import org.dacd_proyect.domain.model.Event;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void shouldCreateEventWithCorrectAttributes() {
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

        assertEquals("ticketmaster", event.getSource());
        assertEquals("event-1", event.getId());
        assertEquals("Test Event", event.getName());
        assertEquals("music", event.getKeyword());
        assertEquals(List.of("Madrid"), event.getCity());
        assertEquals("ES", event.getCountryCode());
        assertEquals("2026-05-30T00:00:00Z", event.getStartDateTime());
        assertEquals("https://example.com/event", event.getUrl());
        assertEquals("40.4168,-3.7038", event.getLatlong());
    }

    @Test
    void shouldHandleMultipleCities() {
        Event event = new Event(
                "ticketmaster",
                "event-2",
                "Multi-city Event",
                "festival",
                List.of("Madrid", "Barcelona", "Valencia"),
                "ES",
                Instant.now(),
                "2026-05-30T00:00:00Z",
                "https://example.com/multi-city-event",
                "40.4168,-3.7038"
        );

        assertEquals(List.of("Madrid", "Barcelona", "Valencia"), event.getCity(), "El evento debería manejar múltiples ciudades");
    }

    @Test
    void shouldHandleEmptyKeyword() {
        Event event = new Event(
                "ticketmaster",
                "event-3",
                "No Keyword Event",
                "",
                List.of("Madrid"),
                "ES",
                Instant.now(),
                "2026-05-30T00:00:00Z",
                "https://example.com/no-keyword-event",
                "40.4168,-3.7038"
        );

        assertEquals("", event.getKeyword(), "El evento debería permitir un campo de keyword vacío");
    }

    @Test
    void shouldHandleNoCity() {
        Event event = new Event(
                "ticketmaster",
                "event-4",
                "No City Event",
                "music",
                List.of(),
                "ES",
                Instant.now(),
                "2026-05-30T00:00:00Z",
                "https://example.com/no-city-event",
                "40.4168,-3.7038"
        );

        assertTrue(event.getCity().isEmpty(), "El evento debería permitir una lista de ciudades vacía");
    }
}

