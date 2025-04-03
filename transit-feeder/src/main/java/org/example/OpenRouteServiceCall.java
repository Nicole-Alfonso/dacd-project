package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;

public class OpenRouteServiceCall {
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
}
