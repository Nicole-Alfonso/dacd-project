import application.HotelProvider;
import domain.model.HotelData;
import org.example.shared.PriceOffer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelProviderTest {

    private static class DummyProvider implements HotelProvider {
        @Override
        public List<HotelData> fetchHotels(String provinceApiKey) {
            return List.of(new HotelData("H001", "Dummy Hotel", "Jaén", "Jaén", "Calle Real",
                    4.0, 37.77, -3.79, List.of(new PriceOffer("Xotelo", 65.0, "EUR"))));
        }
    }

    @Test
    void testFetchHotelsReturnsHotelList() {
        HotelProvider provider = new DummyProvider();
        List<HotelData> hotels = provider.fetchHotels("dummy-key");

        assertEquals(1, hotels.size());
        assertEquals("Dummy Hotel", hotels.get(0).getName());
    }
}
