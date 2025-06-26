
import org.business.Datamart;
import org.business.HotelPriceEventRetriever;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shared.HotelEvent;
import org.shared.PriceOffer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelPriceEventRetrieverTest {

    private Datamart datamart;
    private HotelPriceEventRetriever retriever;

    @BeforeEach
    void setUp() {
        datamart = mock(Datamart.class);
        retriever = new HotelPriceEventRetriever(datamart);
    }

    @Test
    void testRetrieveAndStoreReturnsCorrectHotels() {
        // Arrange
        String city = "Barcelona";
        LocalDate checkIn = LocalDate.of(2025, 8, 10);
        LocalDate checkOut = LocalDate.of(2025, 8, 12);

        HotelEvent hotel = new HotelEvent(
                Instant.parse("2025-06-01T12:00:00Z"), // ts
                "source-system",                      // ss
                "H123",                               // id
                "Barcelona",                          // city
                "Hotel BCN",                          // name
                4.3,                                  // rating
                41.3851,                              // lat
                2.1734,                               // lon
                90.0,                                 // minPrice
                130.0,                                // maxPrice
                "4-star",                             // category
                List.of(new PriceOffer("agency", 100.0, "eur")), // priceOffers
                "http://hotelbcn.com",                // url
                checkIn,                              // checkIn
                checkOut                              // checkOut
        );

        when(datamart.findHotelEvents(city, checkIn, checkOut)).thenReturn(List.of(hotel));

        // Act
        List<HotelEvent> result = retriever.retrieveAndStore(city, checkIn, checkOut, datamart);

        // Assert
        verify(datamart).findHotelEvents(city, checkIn, checkOut);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hotel BCN", result.get(0).getName());
    }

    @Test
    void testRetrieveAndStoreReturnsEmptyWhenNoHotelsFound() {
        // Arrange
        String city = "Valencia";
        LocalDate checkIn = LocalDate.of(2025, 7, 5);
        LocalDate checkOut = LocalDate.of(2025, 7, 8);

        when(datamart.findHotelEvents(city, checkIn, checkOut)).thenReturn(List.of());

        // Act
        List<HotelEvent> result = retriever.retrieveAndStore(city, checkIn, checkOut, datamart);

        // Assert
        verify(datamart).findHotelEvents(city, checkIn, checkOut);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testRetrieveAndStoreDoesNotThrowWhenDatamartReturnsNull() {
        // Arrange
        String city = "Madrid";
        LocalDate checkIn = LocalDate.of(2025, 9, 1);
        LocalDate checkOut = LocalDate.of(2025, 9, 3);

        when(datamart.findHotelEvents(city, checkIn, checkOut)).thenReturn(null);

        // Act
        List<HotelEvent> result = retriever.retrieveAndStore(city, checkIn, checkOut, datamart);

        // Assert
        assertNull(result);
    }
}
