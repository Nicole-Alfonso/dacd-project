package org.business;

import org.business.events.EventStoreLoader;
import org.business.events.LiveEventSubscriber;
import org.shared.HotelFilter;
import org.shared.HotelEvent;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BusinessLauncher {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Uso:");
            System.out.println("  <nombreEvento> <ciudad> <checkIn> <checkOut> [categoria] [precioMax] [minRating] [distanciaMaxKm]");
            return;
        }

        try {
            String nombreEvento = args[0];
            String ciudad = args[1];

            LocalDate checkIn = LocalDate.parse(args[2]);
            LocalDate checkOut = LocalDate.parse(args[3]);

            // Validación de orden lógico de fechas
            if (!checkIn.isBefore(checkOut)) {
                System.err.println("Error: La fecha de check-in debe ser anterior a la de check-out.");
                return;
            }

            String categoria = args.length >= 5 ? args[4] : null;

            double precioMax = args.length >= 6 ? Double.parseDouble(args[5]) : Double.MAX_VALUE;
            double minRating = args.length >= 7 ? Double.parseDouble(args[6]) : 0.0;
            double distanciaMaxKm = args.length >= 8 ? Double.parseDouble(args[7]) : Double.MAX_VALUE;

            if (precioMax < 0 || minRating < 0 || distanciaMaxKm < 0) {
                System.err.println("Error: Los valores numéricos no pueden ser negativos.");
                return;
            }

            HotelFilter filtro = new HotelFilter(categoria, precioMax, minRating, distanciaMaxKm);

            Datamart datamart = new Datamart();
            EventStoreLoader.loadAllEvents(datamart);
            LiveEventSubscriber.startListening(datamart);

            BusinessUnit unit = new BusinessUnit(datamart);
            List<HotelEvent> hoteles = unit.getHotelesParaEvento(nombreEvento, ciudad, checkIn, checkOut, filtro);

            hoteles.forEach(System.out::println);

        } catch (DateTimeParseException e) {
            System.err.println("Error: Formato de fecha inválido. Usa yyyy-MM-dd para checkIn y checkOut.");
        } catch (NumberFormatException e) {
            System.err.println("Error: Los parámetros numéricos (precioMax, minRating, distanciaMaxKm) deben ser válidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}