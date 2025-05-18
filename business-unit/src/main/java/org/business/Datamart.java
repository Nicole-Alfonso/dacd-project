package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Datamart {
    private final Map<String, Map<String, HotelEvent>> hotelesPorCiudad = new HashMap<>();
    private final List<EventInfo> eventos = new ArrayList<>();
    private final List<HotelEvent> hotelEvents = new ArrayList<>();

    public synchronized void addEvent(HotelEvent event) {
        hotelEvents.add(event);
        hotelesPorCiudad
                .computeIfAbsent(event.getCity().toLowerCase(), k -> new HashMap<>())
                .put(event.getName(), event);
    }

    public synchronized void addEvent(EventInfo event) {
        eventos.add(event);
    }

    public List<EventInfo> getEventos() {
        return eventos;
    }

    public Optional<EventInfo> findEventById(String id) {
        return eventos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public List<HotelEvent> findHotelEvents(String city, LocalDate checkIn, LocalDate checkOut) {
        return hotelEvents.stream()
                .filter(e -> e.getCity().equalsIgnoreCase(city)
                        && e.getCheckIn().equals(checkIn)
                        && e.getCheckOut().equals(checkOut))
                .collect(Collectors.toList());
    }
}
