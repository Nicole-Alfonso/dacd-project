package org.shared;

public class HotelFilter {
    private String categoria;
    private double precioMax;
    private double minRating;
    private double distanciaMaxKm;

    public HotelFilter(String categoria, Double precioMax, Double minRating, Double distanciaMaxKm) {
        this.categoria = categoria;
        this.precioMax = precioMax != null ? precioMax : Double.MAX_VALUE;
        this.minRating = minRating != null ? minRating : 0;
        this.distanciaMaxKm = distanciaMaxKm != null ? distanciaMaxKm : Double.MAX_VALUE;
    }

    public String getCategoria() { return categoria; }
    public double getPrecioMax() { return precioMax; }
    public double getMinRating() { return minRating; }
    public double getDistanciaMaxKm() { return distanciaMaxKm; }
}
