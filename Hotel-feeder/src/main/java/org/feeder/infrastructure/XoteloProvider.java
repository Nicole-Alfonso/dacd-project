package org.feeder.infrastructure;

import com.google.gson.*;
import org.feeder.application.HotelProvider;
import org.feeder.model.HotelData;
import org.shared.PriceOffer;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XoteloProvider implements HotelProvider {

    private static final String BASE_HOTELS_URL = "https://data.xotelo.com/api/list?";
    private static final String BASE_OFFERS_URL = "https://data.xotelo.com/api/rates?";

    @Override
    public List<HotelData> fetchHotels(String cityKey, String cityName) {
        List<HotelData> hotelDataList = new ArrayList<>();
        try {
            String hotelsUrl = BASE_HOTELS_URL + "location_key=" + cityKey + "&offset=0&limit=5";

            JsonArray hotels = getJsonArrayFromUrl(hotelsUrl, "result", "list");

            for (JsonElement element : hotels) {
                JsonObject hotelJson = element.getAsJsonObject();

                String id = getJsonElementAsString(hotelJson, "key");
                String name = getJsonElementAsString(hotelJson, "name");

                JsonObject review = hotelJson.getAsJsonObject("review_summary");
                double rating = review != null && review.has("rating") ? review.get("rating").getAsDouble() : 0.0;

                JsonObject geo = hotelJson.getAsJsonObject("geo");
                double lat = geo != null && geo.has("latitude") ? geo.get("latitude").getAsDouble() : 0.0;
                double lon = geo != null && geo.has("longitude") ? geo.get("longitude").getAsDouble() : 0.0;

                List<PriceOffer> offers = fetchOffers(id);
                String url = getJsonElementAsString(hotelJson, "url");

                Instant ts = Instant.now();
                HotelData hotel = new HotelData(id, cityName, name, rating, lat, lon, offers, ts, url);
                hotelDataList.add(hotel);

                Thread.sleep(500); // Pausa entre llamadas
            }

        } catch (Exception e) {
            System.err.println("Error general al obtener hoteles: " + e.getMessage());
            e.printStackTrace();
        }

        return hotelDataList;
    }

    private List<PriceOffer> fetchOffers(String hotelKey) {
        List<PriceOffer> offers = new ArrayList<>();
        try {
            LocalDate today = LocalDate.now();
            LocalDate checkout = today.plusDays(3);

            String url = BASE_OFFERS_URL +
                         "hotel_key=" + hotelKey +
                         "&chk_in=" + today +
                         "&chk_out=" + checkout;

            JsonArray rates = getJsonArrayFromUrl(url, "result", "rates");
            JsonObject resultObj = getJsonObjectFromUrl(url, "result");
            String currency = getJsonElementAsString(resultObj, "currency");

            for (JsonElement rateElement : rates) {
                JsonObject rate = rateElement.getAsJsonObject();
                String provider = getJsonElementAsString(rate, "name");
                double base = getJsonElementAsDouble(rate, "rate");
                double tax = getJsonElementAsDouble(rate, "tax");
                double finalPrice = base + tax;

                offers.add(new PriceOffer(provider, finalPrice, currency));
            }

        } catch (Exception e) {
            System.err.println("Error al obtener ofertas para hotel " + hotelKey + ": " + e.getMessage());
        }

        return offers;
    }

    // Utilidades JSON

    private JsonArray getJsonArrayFromUrl(String urlStr, String... path) throws Exception {
        JsonObject json = getJsonFromUrl(urlStr);
        for (int i = 0; i < path.length - 1; i++) {
            json = getJsonObjectSafe(json, path[i]);
        }
        return json.has(path[path.length - 1]) && json.get(path[path.length - 1]).isJsonArray()
                ? json.getAsJsonArray(path[path.length - 1])
                : new JsonArray();
    }

    private JsonObject getJsonObjectFromUrl(String urlStr, String... path) throws Exception {
        JsonObject json = getJsonFromUrl(urlStr);
        for (String key : path) {
            json = getJsonObjectSafe(json, key);
        }
        return json;
    }

    private JsonObject getJsonFromUrl(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private JsonObject getJsonObjectSafe(JsonObject json, String key) {
        JsonElement element = json.get(key);
        return (element != null && element.isJsonObject()) ? element.getAsJsonObject() : new JsonObject();
    }

    private String getJsonElementAsString(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && element.isJsonPrimitive()) ? element.getAsString() : "";
    }

    private double getJsonElementAsDouble(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && element.isJsonPrimitive()) ? element.getAsDouble() : 0.0;
    }
}
