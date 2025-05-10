package org.dacd_proyect.infrastructure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketmasterProvider {

    private final String apiKey;

    public TicketmasterProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    public void fetchAndPrintEvents(String location, String date) {
        try {
            // Construir la URL real para la API de Ticketmaster con la clave API, ubicación y fecha
            String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + apiKey + "&city=" + location + "&date=" + date;

            // Llamar al método que hace la solicitud HTTP real
            String jsonResponse = getJsonResponseFromApi(url);

            // Imprimir el JSON completo
            System.out.println("Respuesta JSON de la API: " + jsonResponse);

            // Parsear y mostrar los detalles de los eventos
            printEventDetails(jsonResponse);

        } catch (Exception e) {
            System.err.println("Error al obtener eventos de la API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getJsonResponseFromApi(String urlString) throws Exception {
        // Crear la URL y la conexión
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // Leer la respuesta
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Retornar la respuesta JSON como un String
        return response.toString();
    }

    private void printEventDetails(String jsonResponse) {
        // Convertir la respuesta JSON a un objeto JSONObject
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Extraer el array de eventos
        JSONArray eventsArray = jsonObject.getJSONObject("_embedded").getJSONArray("events");

        // Iterar sobre los eventos y mostrar sus detalles
        for (int i = 0; i < eventsArray.length(); i++) {
            JSONObject eventJson = eventsArray.getJSONObject(i);
            String id = eventJson.getString("id");
            String name = eventJson.getString("name");
            String location = eventJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("city");
            String date = eventJson.getJSONObject("dates").getJSONObject("start").getString("localDate");

            // Imprimir los detalles del evento
            System.out.println("Evento ID: " + id + ", Nombre: " + name + ", Ubicación: " + location + ", Fecha: " + date);
        }
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