import org.feeder.application.HotelProvider;
import org.feeder.application.HotelStore;
import org.feeder.model.Ciudades;
import org.feeder.infrastructure.HotelSqliteStore;
import org.feeder.infrastructure.XoteloProvider;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:hotels.db";
        String cityName = "Sevilla";

        String cityKey = Ciudades.getKey(cityName);
        if (cityKey == null) {
            System.err.println("Clave API no encontrada para la ciudad: " + cityName);
            return;
        }

        HotelProvider provider = new XoteloProvider();
        HotelStore store = new HotelSqliteStore(dbUrl);
        XoteloController controller = new XoteloController(provider, store);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Fetching and publishing hotels for: " + cityName);
            controller.fetchSaveAndPublish(cityKey, cityName);
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando el scheduler...");
            scheduler.shutdown();
        }));
    }
}
