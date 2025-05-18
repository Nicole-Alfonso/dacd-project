import org.feeder.model.HotelData;
import org.junit.jupiter.api.Test;
import org.shared.PriceOffer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HotelDataTest {

    @Test
    void shouldAssignCorrectPriceCategory() {
        List<PriceOffer> low = List.of(new PriceOffer("X", 50, "EUR"));
        List<PriceOffer> medium = List.of(new PriceOffer("X", 100, "EUR"));
        List<PriceOffer> high = List.of(new PriceOffer("X", 200, "EUR"));

        assertEquals(HotelData.PriceCategory.LOW, new HotelData("1", "City", "Low", 4.5, 1, 1, low, Instant.now(), "", LocalDate.now(), LocalDate.now()).getCategory());
        assertEquals(HotelData.PriceCategory.MEDIUM, new HotelData("2", "City", "Med", 4.5, 1, 1, medium, Instant.now(), "", LocalDate.now(), LocalDate.now()).getCategory());
        assertEquals(HotelData.PriceCategory.HIGH, new HotelData("3", "City", "High", 4.5, 1, 1, high, Instant.now(), "", LocalDate.now(), LocalDate.now()).getCategory());
    }

    @Test
    void shouldCalculateMinAndMaxPrice() {
        List<PriceOffer> offers = List.of(
                new PriceOffer("A", 80, "EUR"),
                new PriceOffer("B", 120, "EUR"),
                new PriceOffer("C", 100, "EUR")
        );
        HotelData hotel = new HotelData("1", "Test", "City", 4.2, 0, 0, offers, Instant.now(), "", LocalDate.now(), LocalDate.now());

        assertEquals(80, hotel.getMinPrice());
        assertEquals(120, hotel.getMaxPrice());
    }
}
