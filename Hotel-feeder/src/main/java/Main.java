import application.HotelProvider;
import application.HotelStore;
import infrastructure.HotelSqliteStore;
import infrastructure.XoteloProvider;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:hotels.db";
        String provinceName = "Sevilla";  // Puedes cambiar o iterar sobre Ciudades.CIUDADES

        HotelProvider provider = new XoteloProvider();
        HotelStore store = new HotelSqliteStore(dbUrl);
        XoteloController controller = new XoteloController(provider, store);

        // Ejecutar periódicamente (cada 1 hora)
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Fetching and publishing hotels for: " + provinceName);
            controller.fetchSaveAndPublish(provinceName);
        };

        // Ejecutar inmediatamente y luego cada 1 hora
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        // (Opcional) Detener correctamente con shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando el scheduler...");
            scheduler.shutdown();
        }));
    }
}