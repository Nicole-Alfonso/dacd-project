package org.business;

import org.business.events.EventStoreLoader;
import org.business.events.LiveEventSubscriber;
import org.shared.HotelFilter;
import org.shared.HotelEvent;

import java.time.LocalDate;
import java.util.List;

public class BusinessLauncher {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Uso:");
            System.out.println("  <nombreEvento> <ciudad> <checkIn> <checkOut> [categoria] [precioMax] [minRating] [distanciaMaxKm]");
            return;
        }

        String nombreEvento = args[0];
        String ciudad = args[1];
        LocalDate checkIn = LocalDate.parse(args[2]);
        LocalDate checkOut = LocalDate.parse(args[3]);

        String categoria = args.length >= 5 ? args[4] : null;
        double precioMax = args.length >= 6 ? Double.parseDouble(args[5]) : Double.MAX_VALUE;
        double minRating = args.length >= 7 ? Double.parseDouble(args[6]) : 0.0;
        double distanciaMaxKm = args.length >= 8 ? Double.parseDouble(args[7]) : Double.MAX_VALUE;

        HotelFilter filtro = new HotelFilter(categoria, precioMax, minRating, distanciaMaxKm);

        Datamart datamart = new Datamart();
        EventStoreLoader.loadAllEvents(datamart);
        LiveEventSubscriber.startListening(datamart);

        BusinessUnit unit = new BusinessUnit(datamart);
        List<HotelEvent> hoteles = unit.getHotelesParaEvento(nombreEvento, ciudad, checkIn, checkOut, filtro);

        hoteles.forEach(System.out::println);
    }
}
