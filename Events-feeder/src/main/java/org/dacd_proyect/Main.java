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
        List<String> cities = Arrays.asList("Brussels");
        String startDateTime = "2026-02-18T00:00:00Z";

        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        EventStore store = new EventSqliteStore("jdbc:sqlite:events.db");

        TicketmasterController controller = new TicketmasterController(provider, store);
        controller.fetchSaveAndPublish(cities, startDateTime);

        System.out.println("Eventos cargados para " + cities + " el día " + startDateTime);
    }
}


