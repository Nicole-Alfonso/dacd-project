
import org.dacd_proyect.domain.model.Event;
import org.dacd_proyect.infrastructure.TicketmasterProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketmasterProviderTest {

    @Test
    void shouldFetchEventsFromApi() {
        String apiKey = "tu_api_key_aqui"; // Reemplaza con tu API Key real para pruebas en vivo
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);

        List<Event> events = provider.fetchEvents("Madrid", "2026-05-30T00:00:00Z");

        assertFalse(events.isEmpty(), "Los eventos no deberían estar vacíos");
        assertNotNull(events.get(0).getId(), "El campo 'id' no debería ser nulo");
        assertNotNull(events.get(0).getSource(), "El campo 'source' no debería ser nulo");
        assertTrue(events.get(0).getSource().length() > 0, "El campo 'source' no debería estar vacío");
    }

    @Test
    void shouldContainValidLatLong() {
        String apiKey = "tu_api_key_aqui";
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);

        List<Event> events = provider.fetchEvents("Madrid", "2026-05-30T00:00:00Z");

        assertFalse(events.isEmpty(), "Los eventos no deberían estar vacíos");

        Event firstEvent = events.get(0);
        String latlong = firstEvent.getLatlong();

        assertNotNull(latlong, "El campo 'latlong' no debería ser nulo");
        assertTrue(latlong.matches("-?\\d+\\.\\d+,-?\\d+\\.\\d+"), "El campo 'latlong' debería estar en formato 'lat,long'");
    }

    @Test
    void shouldContainValidStartDateTime() {
        String apiKey = "tu_api_key_aqui";
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);

        List<Event> events = provider.fetchEvents("Madrid", "2026-05-30T00:00:00Z");

        assertFalse(events.isEmpty(), "Los eventos no deberían estar vacíos");

        Event firstEvent = events.get(0);
        String startDateTime = firstEvent.getStartDateTime();

        assertNotNull(startDateTime, "El campo 'startDateTime' no debería ser nulo");
        assertTrue(startDateTime.contains("T"), "El campo 'startDateTime' debería contener 'T' como separador de fecha y hora");
    }

    @Test
    void shouldContainValidCityAndCountry() {
        String apiKey = "tu_api_key_aqui";
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);

        List<Event> events = provider.fetchEvents("Madrid", "2026-05-30T00:00:00Z");

        assertFalse(events.isEmpty(), "Los eventos no deberían estar vacíos");

        Event firstEvent = events.get(0);

        assertNotNull(firstEvent.getCity(), "El campo 'city' no debería ser nulo");
        assertFalse(firstEvent.getCity().isEmpty(), "El campo 'city' no debería estar vacío");
        assertNotNull(firstEvent.getCountryCode(), "El campo 'countryCode' no debería ser nulo");
        assertTrue(firstEvent.getCountryCode().length() == 2, "El campo 'countryCode' debería tener dos caracteres");
    }

    @Test
    void shouldContainValidUrl() {
        String apiKey = "tu_api_key_aqui";
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);

        List<Event> events = provider.fetchEvents("Madrid", "2026-05-30T00:00:00Z");

        assertFalse(events.isEmpty(), "Los eventos no deberían estar vacíos");

        Event firstEvent = events.get(0);

        assertNotNull(firstEvent.getUrl(), "El campo 'url' no debería ser nulo");
        assertTrue(firstEvent.getUrl().startsWith("http"), "El campo 'url' debería empezar con 'http'");
    }
}
