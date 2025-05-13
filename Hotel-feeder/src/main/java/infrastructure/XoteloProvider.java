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

                String id = hotelJson.get("key").getAsString();
                String name = hotelJson.get("name").getAsString();
                double rating = hotelJson.getAsJsonObject("review_summary").get("rating").getAsDouble();
                double lat = hotelJson.getAsJsonObject("geo").get("latitude").getAsDouble();
                double lon = hotelJson.getAsJsonObject("geo").get("longitude").getAsDouble();
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
            String currency = getJsonObjectFromUrl(url, "result").get("currency").getAsString();

            for (JsonElement rateElement : rates) {
                JsonObject rate = rateElement.getAsJsonObject();
                String provider = rate.get("name").getAsString();
                double price = rate.get("rate").getAsDouble();

                offers.add(new PriceOffer(provider, price, currency));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener ofertas para hotel " + hotelKey + ": " + e.getMessage());
        }
        return offers;
    }

    // Utilidades para parseo JSON
    private JsonArray getJsonArrayFromUrl(String urlStr, String... path) throws Exception {
        JsonObject json = getJsonFromUrl(urlStr);
        for (int i = 0; i < path.length - 1; i++) {
            json = json.getAsJsonObject(path[i]);
        }
        return json.getAsJsonArray(path[path.length - 1]);
    }

    private JsonObject getJsonObjectFromUrl(String urlStr, String... path) throws Exception {
        JsonObject json = getJsonFromUrl(urlStr);
        for (String key : path) {
            json = json.getAsJsonObject(key);
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
}
