package org.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class HotelDTO {

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("ciudad")
    private String ciudad;

    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("precio_maximo")
    private double precioMaximo;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("check_in")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @JsonProperty("check_out")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    @JsonProperty("url")
    private String url;

    public HotelDTO(String nombre, String ciudad, String categoria, double precioMaximo, double rating,
                    LocalDate checkIn, LocalDate checkOut, String url) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.categoria = categoria;
        this.precioMaximo = precioMaximo;
        this.rating = rating;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.url = url;
    }

    public String getNombre() { return nombre; }
    public String getCiudad() { return ciudad; }
    public String getCategoria() { return categoria; }
    public double getPrecioMaximo() { return precioMaximo; }
    public double getRating() { return rating; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public String getUrl() { return url; }
}
