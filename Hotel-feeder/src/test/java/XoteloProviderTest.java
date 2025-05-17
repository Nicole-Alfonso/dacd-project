import org.feeder.infrastructure.XoteloProvider;
import org.feeder.model.HotelData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XoteloProviderTest {

    @Test
    void shouldFetchHotelsFromApi() {
        XoteloProvider provider = new XoteloProvider();
        List<HotelData> hotels = provider.fetchHotels("g187443");

        assertFalse(hotels.isEmpty());
        assertTrue(hotels.get(0).getId() != null);
        assertTrue(hotels.get(0).getPriceOffers().size() > 0);
    }
}
