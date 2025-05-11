package org.example.business;

import org.example.shared.HotelEvent;
import java.util.*;

public class Datamart {
    private final Map<String, List<HotelEvent>> hotelesPorProvincia = new HashMap<>();

    public synchronized void addEvent(HotelEvent event) {
        hotelesPorProvincia
                .computeIfAbsent(event.province, k -> new ArrayList<>())
                .add(event);
    }

    public List<HotelEvent> getHotelsUnderPrice(String province, double maxPrice) {
        return hotelesPorProvincia.getOrDefault(province, Collections.emptyList())
                .stream()
                .filter(h -> h.priceOffers.stream().anyMatch(p -> p.getPrice() <= maxPrice))
                .toList();
    }

    public List<HotelEvent> getTopRated(String province, int topN) {
        return hotelesPorProvincia.getOrDefault(province, Collections.emptyList())
                .stream()
                .sorted(Comparator.comparingDouble(h -> -h.rating))
                .limit(topN)
                .toList();
    }
}
