package org.shared;

public class HotelFilter {
    private String categoria;             // null = no filtrar por categoría
    private double precioMax;             // Double.MAX_VALUE = no filtrar por precio
    private double minRating;             // 0 = no filtrar por rating
    private double distanciaMaxKm;        // Double.MAX_VALUE = no filtrar por distancia

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
