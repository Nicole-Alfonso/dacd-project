package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;
import org.shared.HotelFilter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Datamart {
    private final List<EventInfo> eventos = new ArrayList<>();
    private final List<HotelEvent> hoteles = new ArrayList<>();
    private final List<HotelEvent> hotelEvents = new ArrayList<>();


    public void addEvent(EventInfo event) {
        eventos.add(event);
    }

    public void addHotel(HotelEvent hotel) {
        hoteles.add(hotel);
    }

    public List<EventInfo> getEventosPorNombreYCiudad(String nombreEvento, String ciudad) {
        return eventos.stream()
                .filter(e -> e.getName().equalsIgnoreCase(nombreEvento) && e.getCity().equalsIgnoreCase(ciudad))
                .collect(Collectors.toList());
    }

    public List<HotelEvent> getHotelesFiltrados(String ciudad, LocalDate checkIn, LocalDate checkOut, HotelFilter filtro, double latEvento, double lonEvento) {
        return hoteles.stream()
                .filter(h -> h.getCity().equalsIgnoreCase(ciudad))
                .filter(h -> !h.getCheckIn().isAfter(checkIn) && !h.getCheckOut().isBefore(checkOut))
                .filter(h -> filtro.getCategoria() == null || h.getCategory().equalsIgnoreCase(filtro.getCategoria()))
                .filter(h -> h.getMaxPrice() <= filtro.getPrecioMax())
                .filter(h -> h.getRating() >= filtro.getMinRating())
                .filter(h -> calcularDistanciaKm(h.getLat(), h.getLon(), latEvento, lonEvento) <= filtro.getDistanciaMaxKm())
                .collect(Collectors.toList());
    }

    private double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public List<HotelEvent> findHotelEvents(String city, LocalDate checkIn, LocalDate checkOut) {
        return hotelEvents.stream()
                .filter(e -> e.getCity().equalsIgnoreCase(city)
                        && !e.getCheckIn().isAfter(checkIn)
                        && !e.getCheckOut().isBefore(checkOut))
                .collect(Collectors.toList());
    }
}
