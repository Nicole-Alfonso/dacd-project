package org.dacd_proyect.infrastructure;

import org.dacd_proyect.application.EventProvider;
import org.dacd_proyect.domain.model.Event;

import okhttp3.*;
import java.io.IOException;
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
    public List<Event> fetchEvents(String location, String date) {
        List<Event> events = new ArrayList<>();

        String url = "https://app.ticketmaster.com/discovery/v2/events.json" +
                "?apikey=" + apiKey +
                "&countryCode=ES" +
                "&city=" + location +
                "&startDateTime=" + date + "T00:00:00Z" +
                "&endDateTime=" + date + "T23:59:59Z" +
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
                System.err.println("No se encontraron eventos para " + location + " en la fecha " + date);
                return events;
            }

            JSONArray eventArray = jsonObject
                    .getJSONObject("_embedded")
                    .getJSONArray("events");

            for (int i = 0; i < eventArray.length(); i++) {
                JSONObject eventJson = eventArray.getJSONObject(i);

                String id = eventJson.getString("id");
                String name = eventJson.optString("name", "Evento sin nombre");
                String urlEvent = eventJson.optString("url", "URL desconocida");

                JSONObject venue = eventJson
                        .getJSONObject("_embedded")
                        .getJSONArray("venues")
                        .getJSONObject(0);

                String venueName = venue.optString("name", "Lugar desconocido");
                String lat = venue.optJSONObject("location").optString("latitude", "0.0");
                String lon = venue.optJSONObject("location").optString("longitude", "0.0");
                String latlong = lat + "," + lon;

                // Usa el campo 'startDateTime' como String
                String eventDate = eventJson
                        .getJSONObject("dates")
                        .getJSONObject("start")
                        .optString("localDate", "Fecha desconocida");

                Event event = new Event(id, name, venueName, eventDate, urlEvent, latlong);
                events.add(event);
            }

        } catch (IOException | JSONException e) {
            System.err.println("Error al procesar eventos de Ticketmaster: " + e.getMessage());
        }

        return events;
    }
}



/*

package org.dacd_proyect.infrastructure;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.dacd_proyect.application.EventProvider;
import org.dacd_proyect.domain.model.Event;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TicketmasterProvider implements EventProvider {

    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private String apiKey;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TicketmasterProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Event> fetchEvents(String location, String date) {
        List<Event> events = new ArrayList<>();

        try {
            String url = BASE_URL + "?apikey=" + apiKey + "&city=" + location + "&startDateTime=" + date + "T00:00:00Z" + "&endDateTime=" + date + "T23:59:59Z";
            Document document = Jsoup.connect(url).ignoreContentType(true).get();
            String jsonResponse = document.body().text();
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if (jsonObject.has("_embedded")) {
                JSONArray eventsArray = jsonObject.getJSONObject("_embedded").getJSONArray("events");

                for (int i = 0; i < eventsArray.length(); i++) {
                    JSONObject eventJson = eventsArray.getJSONObject(i);

                    String eventId = eventJson.optString("id", "Unknown ID");
                    String eventName = eventJson.optString("name", "Unknown Event");
                    String eventDateStr = eventJson.optJSONObject("dates").optJSONObject("start").optString("localDate", "Unknown Date") + "T00:00:00";
                    LocalDateTime eventDate = LocalDateTime.parse(eventDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    String venueName = eventJson.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0).optString("name", "Unknown Venue");
                    String eventUrl = eventJson.optString("url", "No URL");
                    String latlong = eventJson.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0).optJSONObject("location").optString("latitude", "") + "," +
                            eventJson.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0).optJSONObject("location").optString("longitude", "");

                    events.add(new Event(eventId, eventName, venueName, eventDate, eventUrl, latlong));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }
}
*/