package org.feeder;

import org.feeder.application.HotelProvider;
import org.feeder.application.HotelStore;
import org.feeder.infrastructure.HotelSqliteStore;
import org.feeder.infrastructure.XoteloProvider;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java Main <NombreCiudad> <LocationKey>");
            return;
        }

        String cityName = args[0];
        String locationKey = args[1];

        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce la fecha de check-in (yyyy-MM-dd): ");
        LocalDate checkIn = LocalDate.parse(scanner.nextLine());

        System.out.print("Introduce la fecha de check-out (yyyy-MM-dd): ");
        LocalDate checkOut = LocalDate.parse(scanner.nextLine());

        HotelProvider provider = new XoteloProvider();
        HotelStore store = new HotelSqliteStore("jdbc:sqlite:hotels.db");
        XoteloController controller = new XoteloController(provider, store);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            System.out.println("Buscando hoteles en " + cityName + " desde " + checkIn + " hasta " + checkOut);
            controller.fetchSaveAndPublish(locationKey, cityName, checkIn, checkOut);
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Ejecución interrumpida: " + e.getMessage());
            scheduler.shutdown();
        }
    }
}
