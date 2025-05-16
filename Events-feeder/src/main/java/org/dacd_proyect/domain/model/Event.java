
package org.dacd_proyect.domain.model;

import java.time.Instant;
import java.util.List;

public class Event {
    private final String id;
    private final String name;
    private final String keyword;
    private final String venueId;
    private final List<String> city;
    private final String countryCode;
    private final Instant timestamp;
    private final String startDateTime;
    private final String endDateTime;
    private final String url;
    private final String latlong;

    public Event(String id, String name, String keyword, String venueId, List<String> city,
                 String countryCode, Instant timestamp,
                 String startDateTime, String endDateTime, String url, String latlong) {
        this.id = id;
        this.name = name;
        this.keyword = keyword;
        this.venueId = venueId;
        this.city = city;
        this.countryCode = countryCode;
        this.timestamp = timestamp;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.url = url;
        this.latlong = latlong;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getVenueId() {
        return venueId;
    }

    public List<String> getCity() {
        return city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getUrl() {
        return url;
    }

    public String getLatlong() {
        return latlong;
    }
}

