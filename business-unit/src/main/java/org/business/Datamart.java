package org.business;

import org.example.shared.HotelEvent;
import java.util.*;

public class Datamart {
    private final Map<String, List<HotelEvent>> hotelesPorCiudad = new HashMap<>();

    public synchronized void addEvent(HotelEvent event) {
        hotelesPorCiudad
                .computeIfAbsent(event.city, k -> new ArrayList<>())
                .add(event);
    }

    public List<HotelEvent> getHotelsUnderPrice(String city, double maxPrice) {
        return hotelesPorCiudad.getOrDefault(city, Collections.emptyList())
                .stream()
                .filter(h -> h.priceOffers.stream().anyMatch(p -> p.getPrice() <= maxPrice))
                .toList();
    }

    public List<HotelEvent> getTopRated(String city, int topN) {
        return hotelesPorCiudad.getOrDefault(city, Collections.emptyList())
                .stream()
                .sorted(Comparator.comparingDouble(h -> -h.rating))
                .limit(topN)
                .toList();
    }
}
