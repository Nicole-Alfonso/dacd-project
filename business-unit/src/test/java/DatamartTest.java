import org.business.Datamart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shared.HotelEvent;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatamartTest {

    private Datamart datamart;

    @BeforeEach
    void setup() {
        datamart = new Datamart();

        // Hotel 1: cerca y barato
        datamart.addEvent(new HotelEvent(Instant.now(), "Xotelo", "h1", "Hotel Barato", "Sevilla", 4.2,
                37.3886, -5.9823, 75.0, 120.0, "LOW", List.of()));

        // Hotel 2: caro y lejos
        datamart.addEvent(new HotelEvent(Instant.now(), "Xotelo", "h2", "Hotel Caro", "Sevilla", 4.8,
                37.0, -6.0, 200.0, 250.0, "HIGH", List.of()));

        // Hotel 3: medio precio y buena puntuación
        datamart.addEvent(new HotelEvent(Instant.now(), "Xotelo", "h3", "Hotel Medio", "Sevilla", 4.5,
                37.389, -5.983, 120.0, 180.0, "MEDIUM", List.of()));
    }

    @Test
    void testFiltradoPorPrecioYCategoria() {
        var resultados = datamart.getHotelesFiltrados(
                "Sevilla", 37.3886, -5.9823,
                150.0, "LOW", 0.0, Double.MAX_VALUE
        );

        assertEquals(1, resultados.size());
        assertEquals("Hotel Barato", resultados.get(0).getName());
    }

    @Test
    void testFiltradoPorRatingMinimo() {
        var resultados = datamart.getHotelesFiltrados(
                "Sevilla", 37.3886, -5.9823,
                Double.MAX_VALUE, null, 4.6, Double.MAX_VALUE
        );

        assertEquals(1, resultados.size());
        assertEquals("Hotel Caro", resultados.get(0).getName());
    }

    @Test
    void testFiltradoPorDistancia() {
        var resultados = datamart.getHotelesFiltrados(
                "Sevilla", 37.3886, -5.9823,
                Double.MAX_VALUE, null, 0.0, 2.0  // dentro de 2km
        );

        assertEquals(2, resultados.size());
    }

    @Test
    void testSinHotelesEnCiudad() {
        var resultados = datamart.getHotelesFiltrados(
                "Madrid", 40.4, -3.7,
                100.0, null, 0.0, Double.MAX_VALUE
        );

        assertTrue(resultados.isEmpty());
    }
}
