package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Datamart {

    private final Map<String, List<HotelEvent>> hotelesPorCiudad = new HashMap<>();
    private final List<EventInfo> eventos = new ArrayList<>();

    public synchronized void addEvent(HotelEvent event) {
        hotelesPorCiudad
                .computeIfAbsent(event.city, k -> new ArrayList<>())
                .add(event);
    }

    public synchronized void addEvent(EventInfo event) {
        eventos.add(event);
    }

    public List<EventInfo> getEventos() {
        return eventos;
    }

    public EventInfo findEventByName(String name) {
        return eventos.stream()
                .filter(e -> e.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<HotelEvent> getHotelsForEvent(
            String eventName,
            double maxPrice,
            String category,          // LOW, MEDIUM, HIGH
            double minRating,
            double maxDistanceKm
    ) {
        EventInfo evento = findEventByName(eventName);
        if (evento == null) {
            System.out.println("⚠️ Evento no encontrado: " + eventName);
            return List.of();
        }

        String city = evento.city;
        LocalDate eventDate = evento.date;

        return hotelesPorCiudad.getOrDefault(city, Collections.emptyList())
                .stream()
                .filter(h -> h.ts != null && h.ts.atZone(java.time.ZoneId.systemDefault()).toLocalDate().equals(eventDate))
                .filter(h -> h.minPrice <= maxPrice)
                .filter(h -> category == null || h.category.equalsIgnoreCase(category))
                .filter(h -> h.rating >= minRating)
                .filter(h -> {
                    if (evento.latitude == 0 || evento.longitude == 0) return true;
                    double distance = distanceKm(h.latitude, h.longitude, evento.latitude, evento.longitude);
                    return distance <= maxDistanceKm;
                })
                .collect(Collectors.toList());
    }

    // Distancia entre dos coordenadas con la fórmula de Haversine
    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km

        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                   + Math.cos(latRad1) * Math.cos(latRad2)
                     * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
