package org.example.shared;

import java.time.Instant;

public class EventInfo {
    public Instant ts;       // timestamp
    public String ss;        // source (e.g., "Ticketmaster")
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

    @Override
    public String toString() {
        return name + " @ " + city + " on " + date + " | " + url;
    }
}
