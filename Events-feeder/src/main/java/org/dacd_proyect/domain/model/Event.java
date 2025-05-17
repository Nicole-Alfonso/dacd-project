
package org.dacd_proyect.domain.model;

import java.time.Instant;
import java.util.List;

public class Event {
    private final String source;
    private final String id;
    private final String name;
    private final String keyword;
    private final List<String> city;
    private final String countryCode;
    private final Instant timestamp;
    private final String startDateTime;
    private final String url;
    private final String latlong;

    public Event(String source, String id, String name, String keyword, List<String> city,
                 String countryCode, Instant timestamp,
                 String startDateTime, String url, String latlong) {

        this.source = source;
        this.id = id;
        this.name = name;
        this.keyword = keyword;
        this.city = city;
        this.countryCode = countryCode;
        this.timestamp = timestamp;
        this.startDateTime = startDateTime;
        this.url = url;
        this.latlong = latlong;
    }

    public String getSource() {return source; }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
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

    public String getUrl() {
        return url;
    }

    public String getLatlong() {
        return latlong;
    }
}

