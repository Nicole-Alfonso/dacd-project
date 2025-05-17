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
    public List<Event> fetchEvents(String city, String date) {
        List<Event> events = new ArrayList<>();

        String url = "https://app.ticketmaster.com/discovery/v2/events.json" +
                "?apikey=" + apiKey +
                "&city=" + city +
                "&startDateTime=" + date +
                "&size=20";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error en respuesta de Ticketmaster: " + response.code());
                return events;
            }

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);

            if (!jsonObject.has("_embedded")) {
                System.err.println("No se encontraron eventos para " + city + " en la fecha " + date);
                return events;
            }

            JSONArray eventArray = jsonObject.getJSONObject("_embedded").getJSONArray("events");

            for (int i = 0; i < eventArray.length(); i++) {
                JSONObject eventJson = eventArray.getJSONObject(i);


                String id = eventJson.optString("id", "Sin ID");
                String name = eventJson.optString("name", "Sin nombre");
                String urlEvent = eventJson.optString("url", "Sin URL");
                Instant timestamp = Instant.now();

                String latlong = "0.0,0.0";
                if (eventJson.has("_embedded")) {
                    JSONObject venues = eventJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                    if (venues.has("location")) {
                        JSONObject location = venues.getJSONObject("location");
                        latlong = location.optString("latitude", "0.0") + "," + location.optString("longitude", "0.0");
                    }
                }

                String ss = "";
                if (eventJson.has("promoter")) {
                    JSONObject promoter = eventJson.getJSONObject("promoter");
                    ss = promoter.optString("name", "");
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


                Event event = new Event(ss, id, name, keyword, List.of(city), countryCode,
                        timestamp, date, urlEvent, latlong);

                events.add(event);
            }

        } catch (IOException | JSONException e) {
            System.err.println("Error al procesar eventos de Ticketmaster: " + e.getMessage());
        }

        return events;
    }
}

