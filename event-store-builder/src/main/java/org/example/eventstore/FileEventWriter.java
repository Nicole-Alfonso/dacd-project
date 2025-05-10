package org.example.eventstore;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FileEventWriter implements EventWriter {

    @Override
    public void write(String topic, String jsonEvent) {
        try {
            JsonObject obj = JsonParser.parseString(jsonEvent).getAsJsonObject();

            String ss = obj.get("ss").getAsString();
            String ts = obj.get("ts").getAsString();

            LocalDate date = Instant.parse(ts).atZone(ZoneOffset.UTC).toLocalDate();
            String dateStr = date.format(DateTimeFormatter.BASIC_ISO_DATE);

            String dirPath = "eventstore/" + topic + "/" + ss;
            String filePath = dirPath + "/" + dateStr + ".events";

            Files.createDirectories(Paths.get(dirPath));
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(jsonEvent + "\n");
            }

            System.out.println("Evento guardado en: " + filePath);

        } catch (Exception e) {
            System.err.println("Error al guardar evento: " + e.getMessage());
        }
    }
}
