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
        String city = "Madrid";
        String startDateTime = "2026-05-30T00:00:00Z";

        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        EventStore store = new EventSqliteStore("jdbc:sqlite:events.db");

        TicketmasterController controller = new TicketmasterController(provider, store);
        controller.fetchSaveAndPublish(city, startDateTime);

        System.out.println("Eventos cargados para " + city + " el día " + startDateTime);
    }
}


