package org.business.events;

import com.google.gson.*;
import org.shared.EventInfo;
import org.shared.HotelEvent;
import org.shared.InstantTypeAdapter;
import org.business.Datamart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.*;
import java.time.Instant;
import java.util.stream.Stream;

public class EventStoreLoader {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .create();

    public static void loadAllEvents(Datamart datamart) {
        loadHotelEvents("eventstore/HotelPrice/Xotelo", datamart);
        loadConcertEvents("eventstore/TicketmasterEvents/ticketmaster", datamart);
    }

    private static void loadHotelEvents(String folderPath, Datamart datamart) {
        try (Stream<Path> files = Files.walk(Path.of(folderPath))) {
            files.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".events"))
                    .forEach(file -> {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                HotelEvent event = gson.fromJson(line, HotelEvent.class);
                                datamart.addEvent(event);
                            }
                            System.out.println("✔ HotelEvents cargados: " + file);
                        } catch (Exception e) {
                            System.err.println("Error en " + file + ": " + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            System.err.println("Error accediendo a carpeta: " + folderPath);
        }
    }

    private static void loadConcertEvents(String folderPath, Datamart datamart) {
        try (Stream<Path> files = Files.walk(Path.of(folderPath))) {
            files.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".events"))
                    .forEach(file -> {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                EventInfo event = gson.fromJson(line, EventInfo.class);
                                datamart.addEvent(event);
                            }
                            System.out.println("✔ EventInfo cargados: " + file);
                        } catch (Exception e) {
                            System.err.println("Error en " + file + ": " + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            System.err.println("Error accediendo a carpeta: " + folderPath);
        }
    }
}
