import org.feeder.model.HotelData;
import org.junit.jupiter.api.Test;
import org.shared.PriceOffer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HotelDataTest {

    @Test
    void shouldAssignCorrectPriceCategory() {
        List<PriceOffer> low = List.of(new PriceOffer("X", 50, "EUR"));
        List<PriceOffer> medium = List.of(new PriceOffer("X", 100, "EUR"));
        List<PriceOffer> high = List.of(new PriceOffer("X", 200, "EUR"));

        assertEquals(HotelData.PriceCategory.LOW, new HotelData("1", "Low", "City", 4.5, 1, 1, low).getCategory());
        assertEquals(HotelData.PriceCategory.MEDIUM, new HotelData("2", "Med", "City", 4.5, 1, 1, medium).getCategory());
        assertEquals(HotelData.PriceCategory.HIGH, new HotelData("3", "High", "City", 4.5, 1, 1, high).getCategory());
    }

    @Test
    void shouldCalculateMinAndMaxPrice() {
        List<PriceOffer> offers = List.of(
                new PriceOffer("A", 80, "EUR"),
                new PriceOffer("B", 120, "EUR"),
                new PriceOffer("C", 100, "EUR")
        );
        HotelData hotel = new HotelData("1", "Test", "City", 4.2, 0, 0, offers);

        assertEquals(80, hotel.getMinPrice());
        assertEquals(120, hotel.getMaxPrice());
    }
}
