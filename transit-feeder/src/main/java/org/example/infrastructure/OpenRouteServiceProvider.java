package org.example.infrastructure;

import org.example.application.RouteProvider;
import org.example.domain.model.Route;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.List;

public class OpenRouteServiceProvider implements RouteProvider {
    private final String apiKey;

    public OpenRouteServiceProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    private static final String ENDPOINT = "https://api.openrouteservice.org/v2/directions/driving-car?"
            + "&start=2.294351,48.858844&end=2.349902,48.852968";

    public static String getRouteData(String apiKey) throws IOException {
        return Jsoup.connect(ENDPOINT)
                .ignoreContentType(true)
                .header("Authorization", apiKey)
                .method(Connection.Method.GET)
                .execute()
                .body();
    }

    @Override
    public List<Route> provide() {
        return List.of();
    }
}
