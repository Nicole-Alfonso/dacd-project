package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;

public class OpenRouteServiceClient {

    public String getRouteData(String apiKey, String start, String end) throws IOException {
        String endpoint = "https://api.openrouteservice.org/v2/directions/driving-car?&start=" + start + "&end=" + end;

        Connection connection = Jsoup.connect(endpoint)
                .ignoreContentType(true)
                .header("Authorization", apiKey)
                .method(Connection.Method.GET);

        return connection.execute().body();
    }
}

