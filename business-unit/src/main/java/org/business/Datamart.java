package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;

import java.util.*;
import java.util.stream.Collectors;

public class Datamart {
    private final Map<String, List<HotelEvent>> hotelesPorCiudad = new HashMap<>();
    private final List<EventInfo> eventos = new ArrayList<>();

    public synchronized void addEvent(HotelEvent event) {
        hotelesPorCiudad
                .computeIfAbsent(event.getCity().toLowerCase(), k -> new ArrayList<>())
                .add(event);
    }

    public synchronized void addEvent(EventInfo event) {
        eventos.add(event);
    }
    public List<EventInfo> getEventos() {
        return eventos;
    }


    public Optional<EventInfo> findEventoByNombre(String name) {
        return eventos.stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<HotelEvent> getHotelesFiltrados(String ciudad, double eventoLat, double eventoLon,
                                                double maxPrecio, String categoria, double minRating, double maxDistKm) {
        return hotelesPorCiudad.getOrDefault(ciudad.toLowerCase(), Collections.emptyList())
                .stream()
                .filter(h -> h.getMinPrice() <= maxPrecio)
                .filter(h -> categoria == null || h.getCategory().equalsIgnoreCase(categoria))
                .filter(h -> h.getRating() >= minRating)
                .filter(h -> calcularDistanciaKm(eventoLat, eventoLon, h.getLat(), h.getLon()) <= maxDistKm)
                .collect(Collectors.toList());
    }

    private double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
