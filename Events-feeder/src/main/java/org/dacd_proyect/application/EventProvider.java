package org.dacd_proyect.application;

import org.dacd_proyect.domain.model.Event;
import java.util.List;

public interface EventProvider {
    List<Event> fetchEvents(String location, String date) throws Exception;
}



/*
        package org.dacd_proyect.application;

import org.dacd_proyect.domain.model.Event;
import java.util.List;

public interface EventProvider {
    List<Event> provide();
}
*/