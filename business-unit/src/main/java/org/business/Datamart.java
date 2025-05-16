package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;

import java.util.*;
import java.util.stream.Collectors;

public class Datamart {
    private final Map<String, List<HotelEvent>> hotelesPorCiudad = new HashMap<>();
    private final List<EventInfo> eventos = new ArrayList<>();

    public synchronized void addEvent(HotelEvent event) {
        hotelesPorCiudad.computeIfAbsent(event.city, k -> new ArrayList<>()).add(event);
    }

    public synchronized void addEvent(EventInfo event) {
        eventos.add(event);
    }

    public List<EventInfo> getEventos() {
        return eventos;
    }

    public List<HotelEvent> getHotelesFiltrados(String ciudad, double eventoLat, double eventoLon,
                                                double maxPrecio, String categoria, double minRating, double maxDistKm) {
        return hotelesPorCiudad.getOrDefault(ciudad, Collections.emptyList())
                .stream()
                .filter(h -> h.minPrice <= maxPrecio)
                .filter(h -> categoria == null || h.category.equalsIgnoreCase(categoria))
                .filter(h -> h.rating >= minRating)
                .filter(h -> calcularDistanciaKm(eventoLat, eventoLon, h.lat, h.lon) <= maxDistKm)
                .collect(Collectors.toList());
    }

    private double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radio de la Tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}
