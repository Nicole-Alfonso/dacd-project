package org.dacd_proyect;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.infrastructure.TicketmasterProvider;
import org.dacd_proyect.infrastructure.EventSqliteStore;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Comprobar que la API key se ha pasado como argumento
        if (args.length < 1) {
            System.err.println("Uso: java Main <apiKey>");
            System.exit(1);
        }

        String apiKey = args[0];

        Scanner scanner = new Scanner(System.in);

        // Pedir ciudad
        System.out.print("Introduce la ciudad: ");
        String city = scanner.nextLine();

        // Pedir fecha
        System.out.print("Introduce la fecha a partir de la cual buscar eventos (formato YYYY-MM-DD): ");
        String inputDate = scanner.nextLine();

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(inputDate);
        } catch (DateTimeParseException e) {
            System.err.println("Formato de fecha incorrecto. Usa el formato YYYY-MM-DD.");
            return;
        }

        // Configurar provider y store
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        EventStore store = new EventSqliteStore("jdbc:sqlite:events.db");

        TicketmasterController controller = new TicketmasterController(provider, store);
        int eventsPublished = controller.fetchSaveAndPublish(city, localDate);

        if (eventsPublished > 0) {
            System.out.println("Se han cargado " + eventsPublished + " eventos para la ciudad " + city + " a partir del día " + localDate);
        } else {
            System.out.println("No se cargaron eventos para la ciudad " + city + " en la fecha seleccionada.");
        }
    }
}
