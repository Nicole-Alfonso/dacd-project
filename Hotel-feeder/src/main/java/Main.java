import application.HotelProvider;
import application.HotelStore;
import infrastructure.HotelSqliteStore;
import infrastructure.XoteloProvider;
import domain.model.Ciudades;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:hotels.db";
        String cityName = "Sevilla";  // Nombre visible para el usuario

        // Obtener la clave API de la ciudad
        String cityApiKey = Ciudades.getKey(cityName);

        if (cityApiKey == null) {
            System.err.println("Clave API no encontrada para la ciudad: " + cityName);
            return;
        }

        HotelProvider provider = new XoteloProvider();
        HotelStore store = new HotelSqliteStore(dbUrl);
        XoteloController controller = new XoteloController(provider, store);

        // Ejecutar periódicamente (cada 1 hora)
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Fetching and publishing hotels for: " + cityName);
            controller.fetchSaveAndPublish(cityApiKey);
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        // Apagado limpio del scheduler
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando el scheduler...");
            scheduler.shutdown();
        }));
    }
}
