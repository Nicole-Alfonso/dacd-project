package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;

public class OpenRouteServiceCall {

    public static void main(String[] args) {
        String apiKey = args[0];

        String start = "2.294351,48.858844";
        String end = "2.349902,48.852968";

        RoutesDatabase.createTable();

        try {
            OpenRouteServiceClient client = new OpenRouteServiceClient();

            String jsonResponse = client.getRouteData(apiKey, start, end);

            RoutesProcessor processor = new RoutesProcessor();
            Route route = processor.parseRouteData(jsonResponse);

            // Guardar la ruta en la base de datos
            RoutesDatabase.saveRoute(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
