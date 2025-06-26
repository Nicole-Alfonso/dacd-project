import org.junit.jupiter.api.Test;
import org.shared.HotelEvent;
import org.shared.PriceOffer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelEventTest {

    @Test
    public void testConstructorAndGetters() {
        List<PriceOffer> offers = List.of(new PriceOffer("ProvA", 50, "USD"));
        HotelEvent hotel = new HotelEvent(
                Instant.now(), "ss", "h123", "Rome", "Hotel Roma",
                4.2, 41.9, 12.5, 100, 150, "MEDIUM", offers,
                "http://example.com", LocalDate.now(), LocalDate.now().plusDays(2)
        );

        assertEquals("Rome", hotel.getCity());
        assertEquals("Hotel Roma", hotel.getName());
        assertEquals(4.2, hotel.getRating());
        assertEquals(1, hotel.getPriceOffers().size());
    }

    @Test
    public void testToString() {
        HotelEvent hotel = new HotelEvent(
                Instant.now(), "ss", "h123", "Rome", "Hotel Roma",
                4.2, 41.9, 12.5, 100, 150, "MEDIUM",
                List.of(), "http://example.com", LocalDate.now(), LocalDate.now().plusDays(2)
        );

        String str = hotel.toString();
        assertTrue(str.contains("Hotel Roma"));
        assertTrue(str.contains("Rome"));
        assertTrue(str.contains("$100"));
    }
}
