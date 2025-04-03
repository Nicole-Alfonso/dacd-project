package org.example;

import org.example.application.RouteProvider;
import org.example.application.RouteStore;
import org.example.infrastructure.OpenRouteServiceProvider;
import org.example.infrastructure.RouteSqliteStore;

import java.sql.Connection;
import java.sql.SQLException;

public class OpenRouteServiceController {
    private final RouteProvider provider;
    private final RouteStore store;

    public OpenRouteServiceController(RouteProvider provider, RouteStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void run(String apiKey) {
        RouteSqliteStore.createTable();

        try {
            String jsonResponse = OpenRouteServiceProvider.getRouteData(apiKey);
            String coordinates = extractCoordinates(jsonResponse); // Extrae coordenadas como texto

            try (Connection dbConnection = RouteSqliteStore.getConnection()) {
                if (!RouteSqliteStore.isDuplicate(dbConnection, coordinates)) {
                    RouteSqliteStore.insertApiResponse(dbConnection, jsonResponse, coordinates);
                    System.out.println("Datos insertados correctamente.");
                } else {
                    System.out.println("Los datos ya existen en la base de datos.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
        }
    }

    private static String extractCoordinates(String jsonResponse) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(jsonResponse);
            org.json.JSONArray features = json.getJSONArray("features");
            org.json.JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
            org.json.JSONArray coordinates = geometry.getJSONArray("coordinates");

            StringBuilder coordString = new StringBuilder();
            for (int i = 0; i < coordinates.length(); i++) {
                org.json.JSONArray point = coordinates.getJSONArray(i);
                coordString.append(point.getDouble(0)).append(",").append(point.getDouble(1)).append(" | ");
            }

            return coordString.toString();
        } catch (Exception e) {
            System.err.println("Error al extraer coordenadas: " + e.getMessage());
            return "";
        }
    }
}

