
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
        String provinceApiKey = "sevilla";  // ahora sí es de Andalucía

        HotelProvider provider = new XoteloProvider();
        HotelStore store = new HotelSqliteStore(dbUrl);
        XoteloController controller = new XoteloController(provider, store);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Fetching hotels for province: " + provinceApiKey);
            controller.fetchAndSave(provinceApiKey);
            System.out.println("Fetch complete.");
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS); // cada hora

        // Mantener la app viva (opcional para pruebas locales)
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
    }
}
