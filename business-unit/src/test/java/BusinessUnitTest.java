import org.business.BusinessUnit;
import org.business.Datamart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shared.EventInfo;
import org.shared.HotelEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessUnitTest {

    private BusinessUnit unit;

    @BeforeEach
    void setup() {
        unit = new BusinessUnit();
        Datamart datamart = new Datamart();

        datamart.addEvent(new EventInfo(Instant.now(), "Ticketmaster", "Festival Sevilla", "concierto", "Madrid",  "2025-08-01", "https://event.com", 37.3886, -5.9823));

        datamart.addEvent(new HotelEvent(Instant.now(), "Xotelo", "h1", "Sevilla", "Hotel Festival", 4.0,
                37.389, -5.983, 90.0, 150.0, "MEDIUM", List.of(), "", LocalDate.now(), LocalDate.now()));

        // Usamos reflexión para inyectar datamart directamente
        try {
            var field = BusinessUnit.class.getDeclaredField("datamart");
            field.setAccessible(true);
            field.set(unit, datamart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testHotelesParaEventoConFiltros() {
        var hoteles = unit.getHotelesFiltrados("Festival Sevilla", "MEDIUM", 100.0, 3.5, 5.0);
        assertEquals(1, hoteles.size());
        assertEquals("Hotel Festival", hoteles.get(0).getName());
    }

    @Test
    void testEventoNoEncontrado() {
        var hoteles = unit.getHotelesFiltrados("Inexistente", null, 100.0, 0.0, 10.0);
        assertTrue(hoteles.isEmpty());
    }
}
