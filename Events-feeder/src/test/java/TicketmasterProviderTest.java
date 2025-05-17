

import org.dacd_proyect.domain.model.Event;
import org.dacd_proyect.infrastructure.TicketmasterProvider;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketmasterProviderTest {

    private static String apiKey;

    @BeforeAll
    static void setup() {
        apiKey = System.getProperty("ticketmaster.apiKey");
        assertNotNull(apiKey, "La API Key de Ticketmaster es necesaria para ejecutar estos tests");
    }

    @Test
    void shouldFetchEventsFromApi() {
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        List<Event> events = provider.fetchEvents("Madrid", "2026-05-30T00:00:00Z");

        assertFalse(events.isEmpty(), "La lista de eventos no debería estar vacía");
        assertNotNull(events.get(0).getId(), "El evento debería tener un ID");
        assertNotNull(events.get(0).getName(), "El evento debería tener un nombre");
        assertNotNull(events.get(0).getSource(), "El evento debería tener un source");
        assertNotNull(events.get(0).getStartDateTime(), "El evento debería tener una fecha de inicio");
    }
}

