import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.shared.PriceOffer;

public class PriceOfferTest {

    @Test
    void testPriceOfferValues() {
        PriceOffer offer = new PriceOffer("Booking", 89.99, "EUR");

        assertEquals("Booking", offer.getProvider());
        assertEquals(89.99, offer.getPrice());
        assertEquals("EUR", offer.getCurrency());
        assertTrue(offer.toString().contains("Booking"));
    }
}
