import org.example.shared.PriceOffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PriceOfferTest {

    @Test
    void testToString() {
        PriceOffer offer = new PriceOffer("Xotelo", 89.99, "EUR");
        assertEquals("Xotelo: 89.99 EUR", offer.toString());
    }

    @Test
    void testPriceAndCurrency() {
        PriceOffer offer = new PriceOffer("Booking", 120.00, "USD");
        assertEquals(120.00, offer.getPrice());
        assertEquals("USD", offer.getCurrency());
    }
}
