import org.business.Datamart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shared.HotelEvent;
import org.shared.EventInfo;
import org.shared.PriceOffer;
import org.shared.HotelFilter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatamartTest {

    private Datamart datamart;
    private HotelEvent h1, h2;

    @BeforeEach
    void setUp() {
        datamart = new Datamart();

        List<PriceOffer> offers = List.of(new PriceOffer("prov", 100.0, "USD"));
        h1 = new HotelEvent(Instant.now(), "src", "id1", "Madrid", "Hotel Uno",
                4.0, 40.4, -3.7, 80.0, 120.0, "MEDIUM", offers, "url1",
                LocalDate.of(2025,7,1), LocalDate.of(2025,7,10));
        h2 = new HotelEvent(Instant.now(), "src", "id2", "Madrid", "Hotel Dos",
                4.5, 41.4, -3.7, 150.0, 200.0, "HIGH", offers, "url2",
                LocalDate.of(2025,7,3), LocalDate.of(2025,7,8));
    }

    @Test
    void testAddAndGetEventosPorNombreYCiudad() {
        EventInfo e1 = new EventInfo(Instant.now(), "src", "ev1", "Concert", "Madrid",
                LocalDate.of(2025,7,5), "url", 40.4, -3.7);
        datamart.addEvent(e1);

        List<EventInfo> encontrados = datamart.getEventosPorNombreYCiudad("Concert", "Madrid");
        assertEquals(1, encontrados.size());
        assertEquals("ev1", encontrados.get(0).getId());
    }

    @Test
    void testGetHotelesFiltradosByCityAndDate() {
        datamart.addHotel(h1);
        datamart.addHotel(h2);

        HotelFilter filter = new HotelFilter(null, Double.MAX_VALUE, 0.0, 0.0);

        List<HotelEvent> result = datamart.getHotelesFiltrados(
                "Madrid",
                LocalDate.of(2025,7,5),
                LocalDate.of(2025,7,6),
                filter,
                0, 0);

        assertEquals(2, result.size());
    }

    @Test
    void testGetHotelesFiltradosWithPriceAndRatingAndCategory() {
        datamart.addHotel(h1);
        datamart.addHotel(h2);

        HotelFilter filter = new HotelFilter("MEDIUM", 100.0, 4.0, 0.0);

        List<HotelEvent> result = datamart.getHotelesFiltrados(
                "Madrid",
                LocalDate.of(2025,7,2),
                LocalDate.of(2025,7,5),
                filter,
                0, 0);

        assertEquals(1, result.size());
        assertEquals("Hotel Uno", result.get(0).getName());
    }

    @Test
    void testFindHotelEvents() {
        datamart.addHotel(h1);
        List<HotelEvent> found = datamart.findHotelEvents(
                "Madrid",
                LocalDate.of(2025,7,5),
                LocalDate.of(2025,7,9));
        assertEquals(1, found.size());
    }
}
