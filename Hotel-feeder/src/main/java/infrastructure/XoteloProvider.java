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
    public List<HotelData> fetchHotels(String provinceKey) {
        List<HotelData> hotels = new ArrayList<>();

        String url = "https://data.xotelo.com/api/list?location_key=" + provinceKey + "&offset=0&limit=20";

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error al llamar a Xotelo: " + response.code());
                return hotels;
            }

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonHotels = jsonObject.getJSONArray("hotels");

            for (int i = 0; i < jsonHotels.length(); i++) {
                JSONObject h = jsonHotels.getJSONObject(i);

                String id = h.optString("id", "no-id");
                String name = h.optString("name", "Hotel sin nombre");
                String address = h.optString("address", "Desconocido");
                String city = h.optString("city", "Desconocida");
                String province = h.optString("province", "Desconocida");
                double rating = h.optDouble("rating", 0.0);
                double lat = h.optDouble("lat", 0.0);
                double lon = h.optDouble("lon", 0.0);
                double price = h.optDouble("price", 0.0);
                String currency = h.optString("currency", "EUR");

                List<PriceOffer> offers = List.of(new PriceOffer("Xotelo", price, currency));

                HotelData hotelData = new HotelData(
                        id, name, city, province, address, rating, lat, lon, offers
                );

                hotels.add(hotelData);
            }

        } catch (IOException e) {
            System.err.println("Error conectando con Xotelo: " + e.getMessage());
        }

        return hotels;
    }
}
