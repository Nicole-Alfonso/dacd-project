import org.example.business.Datamart;
import org.example.shared.HotelEvent;
import org.example.shared.PriceOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatamartTest {

    private Datamart datamart;

    @BeforeEach
    void setUp() {
        datamart = new Datamart();

        datamart.addEvent(new HotelEvent("Xotelo", "H1", "Hotel A", "Calle 1", "Sevilla", "Sevilla", 4.5, 0, 0,
                List.of(new PriceOffer("Xotelo", 70.0, "EUR"))));

        datamart.addEvent(new HotelEvent("Xotelo", "H2", "Hotel B", "Calle 2", "Sevilla", "Sevilla", 4.8, 0, 0,
                List.of(new PriceOffer("Xotelo", 85.0, "EUR"))));

        datamart.addEvent(new HotelEvent("Xotelo", "H3", "Hotel C", "Calle 3", "Sevilla", "Sevilla", 3.2, 0, 0,
                List.of(new PriceOffer("Xotelo", 50.0, "EUR"))));
    }

    @Test
    void testGetHotelsUnderPrice() {
        List<HotelEvent> baratos = datamart.getHotelsUnderPrice("Sevilla", 75.0);
        assertEquals(2, baratos.size());
    }

    @Test
    void testGetTopRated() {
        List<HotelEvent> top = datamart.getTopRated("Sevilla", 2);
        assertEquals("Hotel B", top.get(0).name);
        assertEquals("Hotel A", top.get(1).name);
    }
}
