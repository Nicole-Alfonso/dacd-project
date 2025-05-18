import org.junit.jupiter.api.Test;
import org.shared.EventInfo;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class EventInfoTest {

    @Test
    void testEventInfoCreation() {
        EventInfo event = new EventInfo(
                Instant.now(), "Ticketmaster", "Concierto Coldplay", "Barcelona",
                "2025-07-15", "https://ticket.com", "", 41.38, 2.17
        );

        assertEquals("ev1", event.getId());
        assertEquals("Barcelona", event.getCity());
        assertEquals("2025-07-15", event.getDate());
        assertEquals("https://ticket.com", event.getUrl());
        assertNotNull(event.getTs());
        assertTrue(event.toString().contains("Coldplay"));
    }
}
