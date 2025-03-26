package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        DatabaseManager.createTable();

        try {
            String endpoint = "https://external.transitapp.com/v3/OTP/Plan";

            // Crear conexión con Jsoup
            Connection connection = Jsoup.connect(endpoint)
                    .ignoreContentType(true)
                    .header("Content-Type", "application/json")//0d0c19a7cfd8707a2660f4b125f6ed11403b82a79772a19dfe332e3a2151387f
                    .data("from", "48.858844,2.294351") // Torre Eiffel
                    .data("to", "48.8566,2.3522") // Notre-Dame
                    .method(Connection.Method.GET);

            // Obtener respuesta en JSON
            String jsonResponse = connection.execute().body();

            DatabaseManager.insertData(jsonResponse);


            System.out.println("Respuesta de Transit API:");
            System.out.println(jsonResponse);


        } catch (IOException e) {
            System.err.println("Error al conectar con la API de Transit: " + e.getMessage());
        }
    }
}
