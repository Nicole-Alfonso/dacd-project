package org.feeder;

import org.feeder.application.HotelProvider;
import org.feeder.application.HotelStore;
import org.feeder.infrastructure.HotelSqliteStore;
import org.feeder.infrastructure.XoteloProvider;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java Main <NombreCiudad> <LocationKey>");
            System.out.println("Ejemplo: java Main Madrid g187514");
            return;
        }

        String cityName = args[0];
        String locationKey = args[1];
        String dbUrl = "jdbc:sqlite:hotels.db";

        HotelProvider provider = new XoteloProvider();
        HotelStore store = new HotelSqliteStore(dbUrl);
        XoteloController controller = new XoteloController(provider, store);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Fetching and publishing hotels for: " + cityName);
            controller.fetchSaveAndPublish(locationKey, cityName);
        };

        // Ejecutar inmediatamente y luego cada hora
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        // Apagar de forma limpia si se termina
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando el scheduler...");
            scheduler.shutdown();
        }));
    }
}
