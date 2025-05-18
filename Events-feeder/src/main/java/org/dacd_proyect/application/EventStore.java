package org.dacd_proyect.application;

import org.dacd_proyect.domain.model.Event;

public interface EventStore {
    void saveEvent(Event event);
}

