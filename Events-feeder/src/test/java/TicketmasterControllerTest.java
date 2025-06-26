import org.dacd_proyect.TicketmasterController;
import org.dacd_proyect.application.EventProvider;
import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.domain.model.Event;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketmasterControllerTest {

    static class DummyProvider implements EventProvider {
        @Override
        public List<Event> fetchEvents(String city, LocalDate date) {
            List<Event> events = new ArrayList<>();
            events.add(new Event(Instant.now(), "Dummy", "id1", "Evento X", "Test", city, "ES",
                    date, "http://dummy.com", 0.0, 0.0));
            return events;
        }
    }

    static class DummyStore implements EventStore {
        public final List<Event> saved = new ArrayList<>();

        @Override
        public void saveEvent(Event event) {
            saved.add(event);
        }
    }

    @Test
    public void testFetchSaveAndPublish() {
        DummyProvider provider = new DummyProvider();
        DummyStore store = new DummyStore();
        TicketmasterController controller = new TicketmasterController(provider, store);

        int published = controller.fetchSaveAndPublish("Madrid", LocalDate.now());

        assertEquals(1, published);
        assertEquals(1, store.saved.size());
        assertEquals("Evento X", store.saved.get(0).getName());
    }
}
