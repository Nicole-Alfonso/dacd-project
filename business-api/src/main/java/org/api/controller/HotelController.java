package org.api.controller;

import org.api.dto.HotelFilterRequest;
import org.business.BusinessUnit;
import org.shared.HotelEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HotelController {

    private final BusinessUnit businessUnit;

    public HotelController() {
        this.businessUnit = new BusinessUnit();
        this.businessUnit.start(); // Carga datos y se suscribe
    }

    @PostMapping("/event")
    public ResponseEntity<?> getHotelsForEvent(@RequestBody HotelFilterRequest request) {
        try {
            List<HotelEvent> hoteles = businessUnit.getHotelesParaEvento(
                    request.getEventName(),
                    request.getCategory(),
                    request.getMaxPrice(),
                    request.getMinRating(),
                    request.getMaxDistanceKm()
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
