package org.feeder.model;

import java.util.HashMap;
import java.util.Map;

public class Ciudades {
    public static final Map<String, String> CIUDADES = new HashMap<>();

    static {
        CIUDADES.put("Sevilla", "g187443");
        CIUDADES.put("Málaga", "g187438");
        CIUDADES.put("Granada", "g187441");
        CIUDADES.put("Cádiz", "g187432");
        CIUDADES.put("Córdoba", "g187430");
        CIUDADES.put("Jaén", "g315916");
        CIUDADES.put("Huelva", "g187442");
        CIUDADES.put("Almería", "g187429");
    }

    public static String getKey(String nombreCiudad) {
        return CIUDADES.get(nombreCiudad);
    }

    public static String getNombreCiudadPorKey(String apiKey) {
        for (Map.Entry<String, String> entry : CIUDADES.entrySet()) {
            if (entry.getValue().equals(apiKey)) {
                return entry.getKey(); // Devuelve "Sevilla" si apiKey == "g187443"
            }
        }
        return null; // No encontrada
    }

}
