package org.api.controller;

import org.business.BusinessUnit;
import org.shared.HotelEvent;
import org.api.dto.HotelFilterRequest;
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
    public List<HotelEvent> getHotelsForEvent(@RequestBody HotelFilterRequest request) {
        return businessUnit.getHotelesParaEvento(
                request.getEventName(),
                request.getCategory(),
                request.getMaxPrice(),
                request.getMinRating(),
                request.getMaxDistanceKm()
        );
    }
}
