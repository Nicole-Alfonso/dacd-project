package org.dacd_proyect.infrastructure;

import org.dacd_proyect.application.EventProvider;
import org.dacd_proyect.domain.model.Event;

import okhttp3.*;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class TicketmasterProvider implements EventProvider {
    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public TicketmasterProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Event> fetchEvents(String city, String startDateTime) {
        List<Event> events = new ArrayList<>();

        String url = "https://app.ticketmaster.com/discovery/v2/events.json" +
                "?apikey=" + apiKey +
                "&city=" + city +
                "&startDateTime=" + startDateTime +
                "&size=20";

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error en respuesta de Ticketmaster: " + response.code());
                return events;
            }

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);

            if (!jsonObject.has("_embedded")) {
                System.err.println("No se encontraron eventos para " + city + " en la fecha " + startDateTime);
                return events;
            }

            JSONArray eventArray = jsonObject.getJSONObject("_embedded").getJSONArray("events");

            for (int i = 0; i < eventArray.length(); i++) {
                JSONObject eventJson = eventArray.getJSONObject(i);

                String id = eventJson.optString("id", "Sin ID");
                String name = eventJson.optString("name", "Sin nombre");
                String urlEvent = eventJson.optString("url", "Sin URL");
                Instant timestamp = Instant.now();

                String latlong = eventJson.optJSONObject("_embedded")
                        .getJSONArray("venues")
                        .getJSONObject(0)
                        .optJSONObject("location")
                        .optString("latitude", "0.0") + "," +
                        eventJson.optJSONObject("_embedded")
                                .getJSONArray("venues")
                                .getJSONObject(0)
                                .optJSONObject("location")
                                .optString("longitude", "0.0");

                double lat = 0.0;
                double lon = 0.0;

                if (latlong.contains(",")) {
                    String[] parts = latlong.split(",");
                    try {
                        lat = Double.parseDouble(parts[0]);
                        lon = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Error al convertir latlong a double: " + e.getMessage());
                    }
                }

                String source = "";
                if (eventJson.has("promoter")) {
                    JSONObject promoter = eventJson.getJSONObject("promoter");
                    source = promoter.optString("name", "");
                }

                String keyword = "";
                if (eventJson.has("classifications")) {
                    JSONArray classifications = eventJson.getJSONArray("classifications");
                    if (classifications.length() > 0) {
                        JSONObject classification = classifications.getJSONObject(0);
                        keyword = classification.optString("segment", "");
                    }
                }

                String countryCode = " ";
                if (eventJson.has("_embedded")) {
                    JSONObject venues = eventJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                    if (venues.has("country")) {
                        JSONObject country = venues.getJSONObject("country");
                        countryCode = country.optString("countryCode", " ");
                    }
                }

                Event event = new Event(source, id, name, keyword, List.of(city), countryCode,
                        timestamp, startDateTime, urlEvent, lat, lon);

                events.add(event);
            }

        } catch (IOException | JSONException e) {
            System.err.println("Error al procesar eventos de Ticketmaster: " + e.getMessage());
        }

        return events;
    }
}