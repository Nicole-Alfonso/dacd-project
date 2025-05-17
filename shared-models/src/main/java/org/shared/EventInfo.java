package org.shared;

import java.time.Instant;

public class EventInfo {
    public Instant ts;
    public String ss;
    public String id;
    public String name;
    public String city;
    public String date;
    public String url;
    public double lat;
    public double lon;

    public EventInfo() {}

    public EventInfo(String ss, String id, String name, String city,
                     String date, String url, double lat, double lon) {
        this.ts = Instant.now();
        this.ss = ss;
        this.id = id;
        this.name = name;
        this.city = city;
        this.date = date;
        this.url = url;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters
    public Instant getTs() { return ts; }
    public String getSs() { return ss; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getDate() { return date; }
    public String getUrl() { return url; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }

    @Override
    public String toString() {
        return name + " @ " + city + " on " + date + " | " + url;
    }
}
