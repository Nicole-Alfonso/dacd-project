import application.HotelProvider;
import application.HotelStore;
import domain.model.HotelData;

import java.util.List;

public class XoteloController {
    private final HotelProvider provider;
    private final HotelStore store;

    public XoteloController(HotelProvider provider, HotelStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void fetchAndSave(String provinceApiKey) {
        List<HotelData> hotels = provider.fetchHotels(provinceApiKey);
        for (HotelData hotel : hotels) {
            store.saveHotel(hotel);
        }
    }
}
