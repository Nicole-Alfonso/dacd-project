package org.dacd_proyect.application;

import org.dacd_proyect.domain.model.Event;

public interface EventStore {
    void save(Event route);
}
