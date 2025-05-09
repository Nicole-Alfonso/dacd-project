package application;

import domain.model.HotelData;
import java.util.List;

public interface HotelProvider {
    List<HotelData> fetchHotels(String provinceApiKey);
}