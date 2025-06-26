import org.dacd_proyect.domain.model.Event;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    public void testEventFieldsAndConversion() {
        Instant ts = Instant.now();
        LocalDate date = LocalDate.of(2025, 6, 25);
        Event event = new Event(ts, "Ticketmaster", "123", "Concierto", "Music",
                "Madrid", "ES", date, "http://evento.com", 40.0, -3.7);

        assertEquals("Madrid", event.getCity());
        assertEquals("Concierto", event.getName());
        assertEquals(date, event.getLocalDate());

        var info = event.toEventInfo();
        assertEquals("Concierto", info.getName());
        assertEquals("Madrid", info.getCity());
    }
}
