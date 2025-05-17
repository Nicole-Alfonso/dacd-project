package org.eventstore.writer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FileEventWriter implements EventWriter {

    private static final String BASE_DIR = "eventstore";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    @Override
    public void write(String topic, String jsonEvent) {
        try {
            JsonObject obj = JsonParser.parseString(jsonEvent).getAsJsonObject();

            String ss = obj.get("ss").getAsString();
            Instant ts = Instant.parse(obj.get("ts").getAsString());
            String dateStr = ts.atZone(ZoneOffset.UTC).toLocalDate().format(DATE_FORMATTER);

            Path dir = Path.of(BASE_DIR, topic, ss);
            Files.createDirectories(dir);

            Path filePath = dir.resolve(dateStr + ".events");
            try (FileWriter writer = new FileWriter(filePath.toFile(), true)) {
                writer.write(jsonEvent + "\n");
            }

            System.out.println("Evento guardado en: " + filePath);

        } catch (Exception e) {
            System.err.println("Error guardando evento: " + e.getMessage());
        }
    }
}
