
package org.dacd_proyect.domain.model;

import java.time.Instant;
import java.util.List;

public class Event {
    private final String ss;
    private final String id;
    private final String name;
    private final String keyword;
    private final List<String> city;
    private final String countryCode;
    private final Instant ts;
    private final String date;
    private final String url;
    private final String latlong;

    public Event(String ss, String id, String name, String keyword, List<String> city,
                 String countryCode, Instant ts,
                 String date, String url, String latlong) {

        this.ss = ss;
        this.id = id;
        this.name = name;
        this.keyword = keyword;
        this.city = city;
        this.countryCode = countryCode;
        this.ts = ts;
        this.date = date;
        this.url = url;
        this.latlong = latlong;
    }

    public String getSource() {return ss; }

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
        return ts;
    }

    public String getStartDateTime() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getLatlong() {
        return latlong;
    }
}

