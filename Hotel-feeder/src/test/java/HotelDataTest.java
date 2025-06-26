
import org.feeder.model.HotelData;
import org.junit.jupiter.api.Test;
import org.shared.PriceOffer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelDataTest {

    @Test
    public void testPriceCategoryLow() {
        List<PriceOffer> offers = List.of(new PriceOffer("Provider", 50.0, "USD"));
        HotelData data = createHotel(offers);
        assertEquals(HotelData.PriceCategory.LOW, data.getCategory());
    }

    @Test
    public void testPriceCategoryMedium() {
        List<PriceOffer> offers = List.of(new PriceOffer("Provider", 100.0, "USD"));
        HotelData data = createHotel(offers);
        assertEquals(HotelData.PriceCategory.MEDIUM, data.getCategory());
    }

    @Test
    public void testPriceCategoryHigh() {
        List<PriceOffer> offers = List.of(new PriceOffer("Provider", 200.0, "USD"));
        HotelData data = createHotel(offers);
        assertEquals(HotelData.PriceCategory.HIGH, data.getCategory());
    }

    @Test
    public void testMinAndMaxPriceCalculation() {
        List<PriceOffer> offers = List.of(
                new PriceOffer("Provider1", 80.0, "USD"),
                new PriceOffer("Provider2", 120.0, "USD")
        );
        HotelData data = createHotel(offers);
        assertEquals(80.0, data.getMinPrice());
        assertEquals(120.0, data.getMaxPrice());
    }

    private HotelData createHotel(List<PriceOffer> offers) {
        return new HotelData("1", "City", "Hotel", 4.2, 10.0, 20.0, offers,
                Instant.now(), "http://example.com", LocalDate.now(), LocalDate.now().plusDays(1));
    }
}
