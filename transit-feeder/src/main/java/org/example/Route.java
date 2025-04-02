package org.example;

public class Route {
    private String start;
    private String end;
    private double distance;
    private double duration;

    public Route(String start, String end, double distance, double duration) {
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.duration = duration;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }
}
