package org.dacd_proyect;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.infrastructure.TicketmasterProvider;
import org.dacd_proyect.infrastructure.EventSqliteStore;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java Main <apiKey>");
            System.exit(1);
        }

        String apiKey = args[0];

        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce la ciudad: ");
        String city = scanner.nextLine();

        System.out.print("Introduce la fecha a partir de la cual buscar eventos (formato YYYY-MM-DD): ");
        String inputDate = scanner.nextLine();

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(inputDate);
        } catch (DateTimeParseException e) {
            System.err.println("Formato de fecha incorrecto. Usa el formato YYYY-MM-DD.");
            return;
        }

        TicketmasterProvider provider = new TicketmasterProvider(apiKey);
        EventStore store = new EventSqliteStore("jdbc:sqlite:events.db");
        TicketmasterController controller = new TicketmasterController(provider, store);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            int eventsPublished = controller.fetchSaveAndPublish(city, localDate);
            if (eventsPublished > 0) {
                System.out.println("Se han cargado " + eventsPublished + " eventos para la ciudad " + city + " a partir del día " + localDate);
            } else {
                System.out.println("No se cargaron eventos para la ciudad " + city + " en la fecha seleccionada.");
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Ejecución interrumpida: " + e.getMessage());
        }
    }
}
