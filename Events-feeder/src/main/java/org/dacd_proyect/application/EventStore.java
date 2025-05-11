package org.dacd_proyect.application;

import org.dacd_proyect.domain.model.Event;
import java.util.List;

public interface EventStore {
    void saveEvent(Event event);
}

