
import org.feeder.XoteloController;
import org.feeder.application.HotelProvider;
import org.feeder.application.HotelStore;
import org.feeder.model.HotelData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.shared.PriceOffer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

public class XoteloControllerTest {

    @Test
    public void testFetchSaveAndPublish_CallsDependencies() {
        HotelProvider mockProvider = mock(HotelProvider.class);
        HotelStore mockStore = mock(HotelStore.class);

        List<PriceOffer> offers = List.of(new PriceOffer("Prov", 99.0, "USD"));
        HotelData hotel = new HotelData("h1", "TestCity", "HotelName", 4.0, 0, 0,
                offers, Instant.now(), "url", LocalDate.now(), LocalDate.now().plusDays(1));

        when(mockProvider.fetchHotels(any(), any(), any(), any())).thenReturn(List.of(hotel));

        XoteloController controller = new XoteloController(mockProvider, mockStore);
        controller.fetchSaveAndPublish("key", "TestCity", LocalDate.now(), LocalDate.now().plusDays(1));

        verify(mockProvider, times(1)).fetchHotels(any(), any(), any(), any());
        verify(mockStore, times(1)).saveHotel(hotel);
    }
}
