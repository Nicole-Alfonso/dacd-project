import org.junit.jupiter.api.Test;
import org.shared.EventInfo;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EventInfoTest {

    @Test
    public void testEqualsAndHashCode() {
        EventInfo e1 = new EventInfo(Instant.now(), "ss1", "id1", "Concert", "Madrid", LocalDate.of(2025, 7, 15), "url1", 40.0, -3.7);
        EventInfo e2 = new EventInfo(Instant.now(), "ss2", "id2", "Concert", "Madrid", LocalDate.of(2025, 7, 15), "url2", 40.1, -3.8);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void testToStringContainsData() {
        EventInfo event = new EventInfo(Instant.now(), "ss", "id", "Festival", "Barcelona", LocalDate.of(2025, 8, 10), "http://link", 41.0, 2.0);
        String s = event.toString();
        assertTrue(s.contains("Festival"));
        assertTrue(s.contains("Barcelona"));
        assertTrue(s.contains("2025"));
    }
}
