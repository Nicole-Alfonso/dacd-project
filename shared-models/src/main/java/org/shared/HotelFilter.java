package org.shared;

public class HotelFilter {
    private String categoria;
    private double maxPrecio;
    private double minRating;
    private double maxDistanciaKm;

    public HotelFilter(String categoria, double maxPrecio, double minRating, double maxDistanciaKm) {
        this.categoria = categoria;
        this.maxPrecio = maxPrecio;
        this.minRating = minRating;
        this.maxDistanciaKm = maxDistanciaKm;
    }

    public String getCategoria() { return categoria; }
    public double getMaxPrecio() { return maxPrecio; }
    public double getMinRating() { return minRating; }
    public double getMaxDistanciaKm() { return maxDistanciaKm; }
}
