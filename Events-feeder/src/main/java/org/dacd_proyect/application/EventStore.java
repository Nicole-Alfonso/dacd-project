package org.dacd_proyect.application;

import org.dacd_proyect.domain.model.Event;
import java.util.List;

public interface EventStore {
    void saveEvents(List<Event> events) throws Exception;
    List<Event> getAllEvents() throws Exception;
}


/*
package org.dacd_proyect.application;


import org.dacd_proyect.domain.model.Event;

public interface EventStore {
    void save(Event route);
}
*/