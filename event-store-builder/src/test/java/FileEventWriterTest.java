import org.eventstore.FileEventWriter;
import org.example.shared.HotelEvent;
import org.example.shared.PriceOffer;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileEventWriterTest {

    @Test
    void testWriteEventToFile() throws Exception {
        // Setup
        FileEventWriter writer = new FileEventWriter();
        HotelEvent event = new HotelEvent("Xotelo", "H001", "Hotel Prueba", "Calle Test", "Granada", "Granada",
                4.0, 0.0, 0.0, List.of(new PriceOffer("Xotelo", 59.99, "EUR")));

        String topic = "HotelPrice";
        String tempDir = "eventstore/" + topic + "/Xotelo";

        // Act
        writer.write(topic, new com.google.gson.Gson().toJson(event));

        // Assert
        File[] files = new File(tempDir).listFiles((dir, name) -> name.endsWith(".events"));
        assertNotNull(files);
        assertTrue(files.length > 0);

        // Leer último archivo creado
        try (BufferedReader reader = new BufferedReader(new FileReader(files[files.length - 1]))) {
            String firstLine = reader.readLine();
            assertTrue(firstLine.contains("Hotel Prueba"));
        }

        // Limpieza (opcional)
        for (File f : files) f.delete();
        Files.deleteIfExists(new File(tempDir).toPath());
    }
}
