import org.eventstore.writer.FileEventWriter;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class FileEventWriterTest {

    private FileEventWriter writer;
    private final String topic = "TestTopic";
    private final String ss = "TestSource";
    private Path baseDir;

    @BeforeEach
    void setUp() {
        writer = new FileEventWriter();
        baseDir = Paths.get("eventstore", topic, ss);
    }

    @AfterEach
    void cleanUp() throws IOException {
        // Elimina archivos de prueba
        if (Files.exists(baseDir)) {
            Files.walk(baseDir)
                    .map(Path::toFile)
                    .sorted((a, b) -> -a.compareTo(b)) // delete files first, then dirs
                    .forEach(File::delete);
        }
    }

    @Test
    void testWriteCreatesFileAndSavesJsonLine() throws Exception {
        String now = Instant.now().toString();

        String jsonEvent = String.format("""
            {
                "ss": "%s",
                "ts": "%s",
                "someField": "value"
            }
            """, ss, now);

        writer.write(topic, jsonEvent);

        // Construye la ruta esperada
        String date = now.substring(0, 10).replace("-", "");
        Path filePath = baseDir.resolve(date + ".events");

        assertTrue(Files.exists(filePath), "El archivo de evento debe existir");

        String content = Files.readString(filePath).trim();
        assertEquals(jsonEvent.trim(), content, "El contenido del archivo debe coincidir");
    }
}
