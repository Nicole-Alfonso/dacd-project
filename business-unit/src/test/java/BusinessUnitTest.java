
import org.business.BusinessUnit;
import org.business.Datamart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shared.HotelEvent;
import org.shared.EventInfo;
import org.shared.HotelFilter;
import org.shared.PriceOffer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusinessUnitTest {

    private Datamart datamart;
    private BusinessUnit unit;
    private HotelEvent h1;
    private EventInfo e1;

    @BeforeEach
    void setUp() {
        datamart = new Datamart();
        unit = new BusinessUnit(datamart);

        List<PriceOffer> offers = List.of(new PriceOffer("prov", 90.0, "USD"));
        h1 = new HotelEvent(Instant.now(), "src", "id1", "Madrid", "Hotel Azul",
                4.0, 40.4, -3.7, 90.0, 120.0, "MEDIUM", offers, "url",
                LocalDate.of(2025,7,1), LocalDate.of(2025,7,10));
        datamart.addHotel(h1);

        e1 = new EventInfo(Instant.now(), "src", "concert1", "Concierto", "Madrid",
                LocalDate.of(2025,7,5), "urlE", 40.4, -3.7);
        datamart.addEvent(e1);
    }

    @Test
    void testGetHotelesParaEventoReturnsHotelWhenEventMatches() {
        HotelFilter filter = new HotelFilter(null, Double.MAX_VALUE, 0.0, Double.MAX_VALUE);

        List<HotelEvent> results = unit.getHotelesParaEvento(
                "Concierto",
                "Madrid",
                LocalDate.of(2025,7,4),
                LocalDate.of(2025,7,6),
                filter);

        assertEquals(1, results.size());
        assertEquals("Hotel Azul", results.get(0).getName());
    }

    @Test
    void testGetHotelesParaEventoEmptyWhenNoEventInRange() {
        HotelFilter filter = new HotelFilter(null, Double.MAX_VALUE, 0.0, Double.MAX_VALUE);

        List<HotelEvent> results = unit.getHotelesParaEvento(
                "Concierto",
                "Madrid",
                LocalDate.of(2025,8,1),
                LocalDate.of(2025,8,5),
                filter);

        assertTrue(results.isEmpty());
    }

    @Test
    void testGetHotelesParaEventoWithFilterExcludesHotel() {
        HotelFilter filter = new HotelFilter(null, 50.0, 0.0, Double.MAX_VALUE); // precioMax < actual

        List<HotelEvent> results = unit.getHotelesParaEvento(
                "Concierto",
                "Madrid",
                LocalDate.of(2025,7,4),
                LocalDate.of(2025,7,6),
                filter);

        assertTrue(results.isEmpty());
    }
}
