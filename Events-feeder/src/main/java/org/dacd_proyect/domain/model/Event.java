package org.dacd_proyect.domain.model;

import java.time.LocalDateTime;

public class Event {
    private final String id;
    private final String name;
    private final String location;
    private final LocalDateTime date;
    private final String url;
    private final String latlong;

    public Event(String id, String name, String location, LocalDateTime date, String url, String latlong) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.url = url;
        this.latlong = latlong;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getLatlong() {
        return latlong;
    }
}



