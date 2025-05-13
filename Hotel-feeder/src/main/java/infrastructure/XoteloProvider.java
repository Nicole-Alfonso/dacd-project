package infrastructure;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import application.HotelProvider;
import domain.model.HotelData;
import org.example.shared.PriceOffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XoteloProvider implements HotelProvider {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<HotelData> fetchHotels(String city) {
        List<HotelData> hotels = new ArrayList<>();

        String cityKey = domain.model.Ciudades.getKey(city); // city == nombreProvincia
        if (cityKey == null) {
            System.err.println("No se encontró clave para la ciudad: " + city);
            return hotels;
        }

        String url = "https://data.xotelo.com/api/list?location_key=" + cityKey + "&offset=0&limit=20";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error al llamar a Xotelo: " + response.code());
                return hotels;
            }

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonHotels = jsonObject.getJSONObject("result").getJSONArray("list");

            for (int i = 0; i < jsonHotels.length(); i++) {
                JSONObject h = jsonHotels.getJSONObject(i);

                String id = h.optString("key", "no-id");
                String name = h.optString("name", "Hotel sin nombre");
                double rating = h.optJSONObject("review_summary") != null
                        ? h.optJSONObject("review_summary").optDouble("rating", 0.0)
                        : 0.0;
                double lat = h.optJSONObject("geo") != null ? h.getJSONObject("geo").optDouble("latitude", 0.0) : 0.0;
                double lon = h.optJSONObject("geo") != null ? h.getJSONObject("geo").optDouble("longitude", 0.0) : 0.0;
                double price = h.optJSONObject("price_ranges") != null
                        ? h.getJSONObject("price_ranges").optDouble("minimum", 0.0)
                        : 0.0;

                List<PriceOffer> offers = List.of(new PriceOffer("Xotelo", price, "EUR"));

                HotelData hotelData = new HotelData(id, name, city, rating, lat, lon, offers);

                hotels.add(hotelData);
            }

        } catch (IOException e) {
            System.err.println("Error conectando con Xotelo: " + e.getMessage());
        }

        return hotels;
    }
}
