package org.business;

import org.shared.EventInfo;
import org.shared.HotelEvent;
import org.shared.FiltroHotel;

import java.util.List;

public class BusinessUnit {
    private final Datamart datamart;

    public BusinessUnit() {
        this.datamart = new Datamart();
    }

    public void start() {
        // Primero cargar histórico, luego empezar a escuchar eventos en tiempo real
        HistoricalEventLoader.loadFromFolder("eventstore/HotelPrice/Xotelo", datamart);
        EventSubscriber.startListening(datamart);
    }

    public List<HotelEvent> getHotelesParaEvento(String nombreEvento, FiltroHotel filtro) {
        return datamart.findEventoByNombre(nombreEvento)
                .map(evento -> datamart.getHotelesFiltrados(
                        evento.getCity(),
                        evento.getLat(),
                        evento.getLon(),
                        filtro))
                .orElse(List.of());
    }

    public List<EventInfo> getEventosDisponibles() {
        return datamart.getEventos();
    }
}
