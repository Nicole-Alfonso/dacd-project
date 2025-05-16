package org.dacd_proyect;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.infrastructure.TicketmasterProvider;
import org.dacd_proyect.infrastructure.EventSqliteStore;
import org.dacd_proyect.TicketmasterController;

import java.util.List;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java Main <apiKey>");
            System.exit(1);
        }

        String apiKey = args[0];
        List<String> cities = Arrays.asList("Madrid", "Barcelona");
        String startDateTime = "2025-07-06T21:00:00Z"; // Formato ISO 8601

        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        EventStore store = new EventSqliteStore("jdbc:sqlite:events.db");

        TicketmasterController controller = new TicketmasterController(provider, store);
        controller.fetchSaveAndPublish(cities, startDateTime);

        System.out.println("Eventos cargados para " + cities + " a partir de " + startDateTime);
    }
}



/*
package org.dacd_proyect;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.infrastructure.TicketmasterProvider;
import org.dacd_proyect.infrastructure.EventSqliteStore;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java Main <apiKey>");
            System.exit(1);
        }

        String apiKey = args[0];
        String location = "Madrid";      // Lugar fijo
        String date = "2025-05-12";      // Fecha fija simple YYYY-MM-DD

        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        EventStore store = new EventSqliteStore("jdbc:sqlite:events.db");

        TicketmasterController controller = new TicketmasterController(provider, store);
        controller.fetchSaveAndPublish(location, date);

        System.out.println("Eventos cargados para " + location + " el día " + date);
    }
}
*/

