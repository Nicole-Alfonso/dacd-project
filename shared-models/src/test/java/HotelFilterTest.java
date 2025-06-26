import org.junit.jupiter.api.Test;
import org.shared.HotelFilter;

import static org.junit.jupiter.api.Assertions.*;

public class HotelFilterTest {

    @Test
    public void testAllValuesProvided() {
        HotelFilter filter = new HotelFilter("Luxury", 200.0, 4.5, 10.0);
        assertEquals("Luxury", filter.getCategoria());
        assertEquals(200.0, filter.getPrecioMax());
        assertEquals(4.5, filter.getMinRating());
        assertEquals(10.0, filter.getDistanciaMaxKm());
    }

    @Test
    public void testNullDefaults() {
        HotelFilter filter = new HotelFilter("Budget", null, null, null);
        assertEquals(Double.MAX_VALUE, filter.getPrecioMax());
        assertEquals(0.0, filter.getMinRating());
        assertEquals(Double.MAX_VALUE, filter.getDistanciaMaxKm());
    }
}
