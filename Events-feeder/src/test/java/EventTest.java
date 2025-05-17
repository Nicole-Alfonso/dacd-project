
import org.dacd_proyect.domain.model.Event;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void shouldCreateEventWithAllFields() {
        Event event = new Event(
                "ticketmaster",
                "event123",
                "Concert in Madrid",
                "Music",
                List.of("Madrid"),
                "ES",
                Instant.now(),
                "2026-05-30T00:00:00Z",
                "https://example.com",
                "40.4168,-3.7038"
        );

        assertEquals("ticketmaster", event.getSource());
        assertEquals("event123", event.getId());
        assertEquals("Concert in Madrid", event.getName());
        assertEquals("Music", event.getKeyword());
        assertEquals(List.of("Madrid"), event.getCity());
        assertEquals("ES", event.getCountryCode());
        assertNotNull(event.getTimestamp());
        assertEquals("2026-05-30T00:00:00Z", event.getStartDateTime());
        assertEquals("https://example.com", event.getUrl());
        assertEquals("40.4168,-3.7038", event.getLatlong());
    }
}
