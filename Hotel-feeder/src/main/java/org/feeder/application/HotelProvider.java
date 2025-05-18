package org.feeder.application;

import org.feeder.model.HotelData;
import java.util.List;

public interface HotelProvider {
    List<HotelData> fetchHotels(String cityKey, String cityName);
}

