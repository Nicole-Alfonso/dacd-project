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
        List<EventInfo> eventos = datamart.getEventosPorNombreYCiudad(nombreEvento, ciudad);

        if (eventos.isEmpty()) {
            System.err.println("No se encontraron eventos con el nombre y ciudad especificados.");
            return List.of();
        }

        // Seleccionar el evento cuya fecha sea más cercana al check-in proporcionado
        EventInfo eventoSeleccionado = eventos.stream()
                .min(Comparator.comparing(e -> Math.abs(ChronoUnit.DAYS.between(e.getDate(), checkIn))))
                .orElse(null);

        if (eventoSeleccionado == null) {
            System.err.println("No se pudo seleccionar un evento adecuado.");
            return List.of();
        }

        List<HotelEvent> hoteles = datamart.getHotelesFiltrados(ciudad, checkIn, checkOut, filtro, eventoSeleccionado.getLat(), eventoSeleccionado.getLon());

        if (hoteles.isEmpty()) {
            System.out.println("No se encontraron hoteles locales. Recuperando dinámicamente...");
            hoteles = retriever.retrieveAndStore(ciudad, checkIn, checkOut, datamart);
            hoteles = datamart.getHotelesFiltrados(ciudad, checkIn, checkOut, filtro, eventoSeleccionado.getLat(), eventoSeleccionado.getLon());
        }

        return hoteles;
    }
}
