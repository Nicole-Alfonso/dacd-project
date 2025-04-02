package org.example;

import org.json.JSONObject;

public class RoutesProcessor {

    // Procesa el JSON de la respuesta de la API y extrae los datos necesarios
    public Route parseRouteData(String jsonResponse) {
        JSONObject response = new JSONObject(jsonResponse);

        // Extraer los datos de distancia y duración
        double distance = response.getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("properties")
                .getJSONArray("segments")
                .getJSONObject(0)
                .getDouble("distance");

        double duration = response.getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("properties")
                .getJSONArray("segments")
                .getJSONObject(0)
                .getDouble("duration");

        // Coordenadas de inicio y fin
        String start = "48.858844,2.294351"; // Coordenadas de inicio (ejemplo)
        String end = "48.852968,2.349902";   // Coordenadas de fin (ejemplo)

        return new Route(start, end, distance, duration); // Retorna un objeto Route
    }
}

