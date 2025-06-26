import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.shared.LocalDateTypeAdapter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateTypeAdapterTest {

    @Test
    public void testSerializationDeserialization() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();

        LocalDate today = LocalDate.of(2025, 6, 26);
        String json = gson.toJson(today);
        LocalDate parsed = gson.fromJson(json, LocalDate.class);

        assertEquals(today, parsed);
    }
}
