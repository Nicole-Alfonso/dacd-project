package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;
import org.shared.HotelFilter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class BusinessUnit {
    private final Datamart datamart;
    private final HotelPriceEventRetriever retriever;

    public BusinessUnit(Datamart datamart) {
        this.datamart = datamart;
        this.retriever = new HotelPriceEventRetriever(datamart);
    }

    public List<HotelEvent> getHotelesParaEvento(String nombreEvento, String ciudad, LocalDate checkIn, LocalDate checkOut, HotelFilter filtro) {
        // Obtener eventos por nombre y ciudad
        List<EventInfo> eventos = datamart.getEventosPorNombreYCiudad(nombreEvento, ciudad);

        System.out.println("\n=== [INFO] Datos Cargados Inicialmente ===");
        System.out.println("Eventos encontrados: " + eventos.size());
        int hotelesIniciales = datamart.getHotelesFiltrados(ciudad, checkIn, checkOut, new HotelFilter("ALL", Double.MAX_VALUE, 0.0, 0.0), 0, 0).size();

        for (EventInfo e : eventos) {
            System.out.printf("Evento: %s, Ciudad: %s, Fecha: %s%n", e.getName(), e.getCity(), e.getLocalDate());
        }

        // Filtrar eventos por fecha
        List<EventInfo> eventosFiltrados = eventos.stream()
                .filter(e -> e.getLocalDate() != null && !e.getLocalDate().isBefore(checkIn) && !e.getLocalDate().isAfter(checkOut))
                .distinct()
                .toList();

        if (eventosFiltrados.isEmpty()) {
            System.err.println("No hay eventos en el rango de fechas especificado para la ciudad y nombre dados.");
            return List.of();
        }

        // Seleccionar evento más cercano a la fecha de check-in
        EventInfo eventoSeleccionado = eventosFiltrados.stream()
                .min(Comparator.comparing(e -> Math.abs(ChronoUnit.DAYS.between(e.getLocalDate(), checkIn))))
                .orElse(null);

        if (eventoSeleccionado == null) {
            System.err.println("No se pudo seleccionar un evento adecuado.");
            return List.of();
        }

        // Buscar hoteles cercanos al evento
        List<HotelEvent> hoteles = datamart.getHotelesFiltrados(
                ciudad, checkIn, checkOut, filtro, eventoSeleccionado.getLat(), eventoSeleccionado.getLon()
        );

        if (hoteles.isEmpty()) {
            System.out.println("\nNo se encontraron hoteles con el filtro aplicado. Cargando desde disco...");
            List<HotelEvent> recuperados = retriever.retrieveAndStore(ciudad, checkIn, checkOut, datamart);
            System.out.println("Hoteles recuperados desde disco: " + recuperados.size());

            hoteles = datamart.getHotelesFiltrados(
                    ciudad, checkIn, checkOut, filtro, eventoSeleccionado.getLat(), eventoSeleccionado.getLon()
            );
        }

        // Llamar al metodo para imprimir de forma bonita
        printHotelesBonito(hoteles);

        return hoteles;
    }

    private void printHotelesBonito(List<HotelEvent> hoteles) {
        System.out.println("\n=== Hoteles Disponibles ===");
        for (HotelEvent hotel : hoteles) {
            System.out.printf(" %s (%s)%n", hotel.getName(), hotel.getCity());
            System.out.println("    • Categoría: " + hotel.getCategory());
            System.out.printf("    • Precio Máximo: $%.2f%n", hotel.getMaxPrice());
            System.out.printf("    • Rating: %.1f%n", hotel.getRating());
            System.out.printf("    • Check-In: %s | Check-Out: %s%n", hotel.getCheckIn(), hotel.getCheckOut());
            if (hotel.getUrl() != null && !hotel.getUrl().isBlank()) {
                System.out.println("    • URL: " + hotel.getUrl());
            }
            System.out.println(); // Separador entre hoteles
        }
    }
}
