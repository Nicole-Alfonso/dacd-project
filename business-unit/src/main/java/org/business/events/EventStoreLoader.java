package org.business.events;

import com.google.gson.*;
import org.shared.EventInfo;
import org.shared.HotelEvent;
import org.shared.InstantTypeAdapter;
import org.business.Datamart;
import org.shared.LocalDateTypeAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.Stream;

public class EventStoreLoader {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    public static void loadAllEvents(Datamart datamart) {
        int hotelCount = loadHotelEvents("eventstore/HotelPrice/Xotelo", datamart);
        int concertCount = loadConcertEvents("eventstore/TicketmasterEvents/ticketmaster", datamart);
        System.out.println("Carga completa: " + hotelCount + " hoteles y " + concertCount + " eventos cargados.");
    }

    private static int loadHotelEvents(String folderPath, Datamart datamart) {
        int count = 0;
        try (Stream<Path> files = Files.walk(Path.of(folderPath))) {
            for (Path file : (Iterable<Path>) files.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".events"))::iterator) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        HotelEvent event = gson.fromJson(line, HotelEvent.class);
                        datamart.addHotel(event);
                        count++;
                    }
                } catch (Exception e) {
                    System.err.println("Error en " + file + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error accediendo a carpeta: " + folderPath);
        }
        return count;
    }

    private static int loadConcertEvents(String folderPath, Datamart datamart) {
        int count = 0;
        try (Stream<Path> files = Files.walk(Path.of(folderPath))) {
            for (Path file : (Iterable<Path>) files.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".events"))::iterator) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        EventInfo event = gson.fromJson(line, EventInfo.class);
                        datamart.addEvent(event);
                        count++;
                    }
                } catch (Exception e) {
                    System.err.println("Error en " + file + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error accediendo a carpeta: " + folderPath);
        }
        return count;
    }
}