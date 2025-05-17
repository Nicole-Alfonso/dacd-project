import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.shared.InstantTypeAdapter;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class InstantTypeAdapterTest {

    @Test
    void testInstantSerializationDeserialization() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        Instant now = Instant.now();
        String json = gson.toJson(now, Instant.class);

        Instant parsed = gson.fromJson(json, Instant.class);
        assertEquals(now.toString(), parsed.toString());
    }
}
