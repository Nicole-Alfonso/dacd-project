package org.dacd_proyect.application;

import org.dacd_proyect.domain.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventProvider {
    List<Event> fetchEvents(String location, LocalDate date);
}
