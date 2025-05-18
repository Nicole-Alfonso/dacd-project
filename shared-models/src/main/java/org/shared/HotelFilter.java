package org.shared;

public class HotelFilter {
    private String categoria;
    private double precioMax;
    private double minRating;
    private double distanciaMaxKm;

    public HotelFilter(String categoria, double precioMax, double minRating, double distanciaMaxKm) {
        this.categoria = categoria;
        this.precioMax = precioMax;
        this.minRating = minRating;
        this.distanciaMaxKm = distanciaMaxKm;
    }

    public String getCategoria() { return categoria; }
    public double getPrecioMax() { return precioMax; }
    public double getMinRating() { return minRating; }
    public double getDistanciaMaxKm() { return distanciaMaxKm; }
}
