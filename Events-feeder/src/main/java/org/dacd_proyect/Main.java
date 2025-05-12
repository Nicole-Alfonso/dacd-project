package org.dacd_proyect;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.infrastructure.TicketmasterProvider;
import org.dacd_proyect.infrastructure.EventSqliteStore;

public class Main {
    public static void main(String[] args) {

        String apiKey = args[0];
        String location = "Madrid";
        String date = "2025-05-12";

        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        EventStore store = new EventSqliteStore("jdbc:sqlite:events.db");

        TicketmasterController controller = new TicketmasterController(provider, store);
        controller.fetchSaveAndPublish(location, date);
    }
}
