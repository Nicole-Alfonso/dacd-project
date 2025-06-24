package org.dacd_proyect.domain.model;

import java.time.Instant;
import java.time.LocalDate;

public class Event {
    private Instant ts;
    private final String ss;
    private final String id;
    private final String name;
    private final String keyword;
    private final String city;
    private final String countryCode;
    private final LocalDate localDate;
    private final String url;
    private final double lat;
    private final double lon;

    public Event(Instant ts, String ss, String id, String name, String keyword, String city,
                 String countryCode, LocalDate localDate, String url,
                 double lat, double lon) {
        this.ts = ts;
        this.ss = ss;
        this.id = id;
        this.name = name;
        this.keyword = keyword;
        this.city = city;
        this.countryCode = countryCode;
        this.localDate = localDate;
        this.url = url;
        this.lat = lat;
        this.lon = lon;
    }

    public Instant getTs() { return ts; }
    public String getSource() { return ss; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getKeyword() { return keyword; }
    public String getCity() { return city; }
    public String getCountryCode() { return countryCode; }
    public LocalDate getLocalDate() { return localDate; }
    public String getUrl() { return url; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }

    public org.shared.EventInfo toEventInfo() {
        return new org.shared.EventInfo(
                this.ts = ts,
                this.ss,
                this.id,
                this.name,
                this.city,
                this.localDate,
                this.url,
                this.lat,
                this.lon
        );
    }
}