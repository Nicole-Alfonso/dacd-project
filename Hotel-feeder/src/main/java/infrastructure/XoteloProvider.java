package infrastructure;

import application.HotelProvider;
import com.google.gson.*;
import domain.model.HotelData;
import org.example.shared.PriceOffer;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XoteloProvider implements HotelProvider {

    private static final String BASE_HOTELS_URL = "https://data.xotelo.com/api/list?";
    private static final String BASE_OFFERS_URL = "https://data.xotelo.com/api/rates?";

    @Override
    public List<HotelData> fetchHotels(String cityKey) {
        List<HotelData> hotelDataList = new ArrayList<>();
        try {
            // Llamada a la API de hoteles
            String hotelsUrl = BASE_HOTELS_URL + "city_key=" + cityKey + "&offset=0&limit=20";
            JsonArray hotels = getJsonArrayFromUrl(hotelsUrl, "result", "list");

            for (JsonElement element : hotels) {
                JsonObject hotelJson = element.getAsJsonObject();

                // Comprobación de valores nulos con un método de ayuda
                String id = getJsonElementAsString(hotelJson, "key");
                String name = getJsonElementAsString(hotelJson, "name");
                double rating = getJsonElementAsDouble(hotelJson, "review_summary", "rating");
                double lat = getJsonElementAsDouble(hotelJson, "geo", "latitude");
                double lon = getJsonElementAsDouble(hotelJson, "geo", "longitude");
                String city = cityKey;  // Aquí podrías usar el cityKey como identificador

                // Llamada a la API de ofertas
                List<PriceOffer> offers = fetchOffers(id);

                // Crear objeto HotelData
                HotelData hotel = new HotelData(id, name, city, rating, lat, lon, offers);
                hotelDataList.add(hotel);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener hoteles: " + e.getMessage());
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
            String currency = getJsonElementAsString(getJsonObjectFromUrl(url, "result"), "currency");

            for (JsonElement rateElement : rates) {
                JsonObject rate = rateElement.getAsJsonObject();
                String provider = getJsonElementAsString(rate, "name");
                double price = getJsonElementAsDouble(rate, "rate");

                offers.add(new PriceOffer(provider, price, currency));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener ofertas para hotel " + hotelKey + ": " + e.getMessage());
        }
        return offers;
    }

    // Utilidades para parseo JSON con verificación de valores nulos
    private JsonArray getJsonArrayFromUrl(String urlStr, String... path) throws Exception {
        JsonObject json = getJsonFromUrl(urlStr);
        for (int i = 0; i < path.length - 1; i++) {
            json = getJsonObjectSafe(json, path[i]);  // Nueva verificación
        }
        return json.has(path[path.length - 1]) && json.get(path[path.length - 1]).isJsonArray() ? json.getAsJsonArray(path[path.length - 1]) : new JsonArray();  // Comprobación adicional
    }

    private JsonObject getJsonObjectFromUrl(String urlStr, String... path) throws Exception {
        JsonObject json = getJsonFromUrl(urlStr);
        for (String key : path) {
            json = getJsonObjectSafe(json, key);  // Nueva verificación
        }
        return json;
    }

    private JsonObject getJsonFromUrl(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    // Nueva función para devolver un JsonObject seguro, evitando NullPointerException
    private JsonObject getJsonObjectSafe(JsonObject json, String key) {
        JsonElement element = json.get(key);
        if (element != null && !element.isJsonNull()) {
            return element.getAsJsonObject();
        }
        return new JsonObject();  // Devuelve un JsonObject vacío si el valor es nulo o no existe
    }

    // Métodos de ayuda para manejar valores nulos de forma segura
    private String getJsonElementAsString(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            return element.getAsString();
        }
        return ""; // Retorna una cadena vacía si el valor es nulo o no existe
    }

    private double getJsonElementAsDouble(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            return element.getAsDouble();
        }
        return 0.0; // Retorna 0.0 si el valor es nulo o no existe
    }

    private double getJsonElementAsDouble(JsonObject jsonObject, String parentKey, String childKey) {
        JsonObject parentObject = jsonObject.getAsJsonObject(parentKey);
        if (parentObject != null && !parentObject.isJsonNull()) {
            return getJsonElementAsDouble(parentObject, childKey);
        }
        return 0.0; // Retorna 0.0 si el valor es nulo o no existe
    }
}
