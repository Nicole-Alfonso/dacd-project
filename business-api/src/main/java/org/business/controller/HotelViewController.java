package org.business.controller;

import org.business.BusinessUnit;
import org.business.Datamart;
import org.business.events.EventStoreLoader;
import org.shared.HotelEvent;
import org.shared.HotelFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HotelViewController {

    private final BusinessUnit unit;

    public HotelViewController() {
        Datamart datamart = new Datamart();
        EventStoreLoader.loadAllEvents(datamart);
        this.unit = new BusinessUnit(datamart);
    }

    // Página con el formulario
    @GetMapping("/")
    public String mostrarFormulario() {
        return "formulario"; // plantilla formulario.html
    }

    // Procesar filtro y mostrar resultados
    @PostMapping("/filtrar")
    public String filtrarHoteles(
            @RequestParam(name = "evento") String evento,
            @RequestParam(name = "ciudad") String ciudad,
            @RequestParam(name = "checkIn") String checkIn,
            @RequestParam(name = "checkOut") String checkOut,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "precioMax", required = false, defaultValue = "999999") double precioMax,
            @RequestParam(name = "minRating", required = false, defaultValue = "0.0") double minRating,
            @RequestParam(name = "distanciaMaxKm", required = false, defaultValue = "999999") double distanciaMaxKm,
            Model model) {

        // Normalizar categoría para que filtro entienda que no hay filtro si está vacía o null
        if (categoria != null && categoria.trim().isEmpty()) {
            categoria = null;
        }

        // Construir filtro con valores reales recibidos
        HotelFilter filtro = new HotelFilter(categoria, precioMax, minRating, distanciaMaxKm);

        LocalDate in = LocalDate.parse(checkIn);
        LocalDate out = LocalDate.parse(checkOut);

        List<HotelEvent> hoteles = unit.getHotelesParaEvento(evento, ciudad, in, out, filtro);

        model.addAttribute("hoteles", hoteles);
        model.addAttribute("total", hoteles.size());

        return "resultados";
    }
}