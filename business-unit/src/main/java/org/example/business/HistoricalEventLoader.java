package org.example.business;

import com.google.gson.Gson;
import org.example.shared.HotelEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class HistoricalEventLoader {

    public static void loadFromFolder(String folderPath, Datamart datamart) {
        Gson gson = new Gson();

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
                            System.out.println("Cargado: " + file);
                        } catch (Exception e) {
                            System.err.println("Error al leer archivo: " + file + ": " + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            System.err.println("Error accediendo a carpeta: " + folderPath + ": " + e.getMessage());
        }
    }
}
