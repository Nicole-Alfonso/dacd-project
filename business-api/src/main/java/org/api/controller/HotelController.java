package org.api.controller;

import org.api.dto.HotelFilterRequest;
import org.business.BusinessUnit;
import org.business.Datamart;
import org.business.events.LiveEventSubscriber;
import org.business.events.EventStoreLoader;
import org.shared.HotelEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HotelController {

    private final BusinessUnit businessUnit;

    public HotelController() {
        Datamart datamart = new Datamart();
        EventStoreLoader.loadAllEvents(datamart);
        LiveEventSubscriber.startListening(datamart);
        this.businessUnit = new BusinessUnit(datamart);
    }

    @PostMapping("/event")
    public ResponseEntity<?> getHotelsForEvent(@RequestBody HotelFilterRequest request) {
        try {
            LocalDate checkIn = request.getCheckIn();
            int nights = request.getNights();
            LocalDate checkOut = checkIn.plusDays(nights);

            List<HotelEvent> hoteles = businessUnit.getHotelesParaEvento(
                    request.getEventName(),
                    request.getCity(),
                    checkIn,
                    checkOut,
                    request.toFilter()
            );

            if (hoteles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron hoteles para el evento '" + request.getEventName() + "'");
            }

            return ResponseEntity.ok(hoteles);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al buscar hoteles: " + e.getMessage());
        }
    }
}
