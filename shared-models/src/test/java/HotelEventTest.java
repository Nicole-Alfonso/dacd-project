import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import org.shared.HotelEvent;
import org.shared.PriceOffer;

import static org.junit.jupiter.api.Assertions.*;

public class HotelEventTest {

    @Test
    void testHotelEventCreation() {
        PriceOffer offer = new PriceOffer("TestProvider", 120.0, "EUR");
        HotelEvent hotel = new HotelEvent(
                Instant.now(),"Xotelo", "h1", "Hotel Test", "Madrid",
                4.5, 40.41, -3.70,
                100, 130, "MEDIUM", List.of(offer)
        );

        assertEquals("h1", hotel.getId());
        assertEquals("Hotel Test", hotel.getName());
        assertEquals(4.5, hotel.getRating());
        assertEquals("MEDIUM", hotel.getCategory());
        assertEquals(1, hotel.getPriceOffers().size());
        assertNotNull(hotel.getTs());
        assertTrue(hotel.toString().contains("Hotel Test"));
    }
}
