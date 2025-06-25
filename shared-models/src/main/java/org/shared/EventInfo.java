package org.shared;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class EventInfo {
    private final Instant ts;
    private final String ss;
    private final String id;
    private final String name;
    private final String city;
    private final LocalDate localDate;
    private final String url;
    private final double lat;
    private final double lon;

    public EventInfo(Instant ts, String ss, String id, String name, String city,
                     LocalDate localDate, String url, double lat, double lon) {
        this.ts = ts;  // Usamos el timestamp que llega, no Instant.now()
        this.ss = ss;
        this.id = id;
        this.name = name;
        this.city = city;
        this.localDate = localDate;
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
    public LocalDate getLocalDate() { return localDate; }
    public String getUrl() { return url; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }

    @Override
    public String toString() {
        return name + " @ " + city + " on " + localDate + " | " + url;
    }

    // equals y hashCode para evitar duplicados basados en nombre, ciudad y fecha
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventInfo)) return false;
        EventInfo that = (EventInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(city, that.city) &&
                Objects.equals(localDate, that.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, localDate);
    }
}
