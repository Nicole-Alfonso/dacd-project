
import domain.model.HotelData;
import org.example.shared.PriceOffer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelDataTest {

    @Test
    void testConstructorAndGetters() {
        List<PriceOffer> offers = List.of(new PriceOffer("Xotelo", 50.0, "EUR"));
        HotelData hotel = new HotelData("H001", "Hotel Prueba", "Cádiz", "Cádiz", "Av. Test",
                4.2, 36.52, -6.28, offers);

        assertEquals("H001", hotel.getId());
        assertEquals("Hotel Prueba", hotel.getName());
        assertEquals("Cádiz", hotel.getProvince());
        assertEquals(4.2, hotel.getRating());
        assertEquals(36.52, hotel.getLatitude());
        assertEquals(offers, hotel.getPriceOffers());
    }

    @Test
    void testToStringIncludesOffers() {
        List<PriceOffer> offers = List.of(new PriceOffer("Xotelo", 60.0, "EUR"));
        HotelData hotel = new HotelData("H002", "Hotel Central", "Málaga", "Málaga", "Calle Falsa",
                3.9, 36.71, -4.42, offers);

        String output = hotel.toString();
        assertTrue(output.contains("Hotel Central"));
        assertTrue(output.contains("Price Offers"));
        assertTrue(output.contains("Xotelo: 60.00 EUR"));
    }
}