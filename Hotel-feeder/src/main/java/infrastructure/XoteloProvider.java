package infrastructure;

import okhttp3.*;
import application.HotelProvider;
import domain.model.Ciudades;
import domain.model.HotelData;
import domain.model.PriceOffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class XoteloProvider implements HotelProvider {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<HotelData> fetchHotels(String cityName) {
        List<HotelData> hotels = new ArrayList<>();

        String locationKey = Ciudades.CIUDADES.get(cityName);
        if (locationKey == null) {
            System.err.println("Provincia no válida: " + cityName);
            return hotels;
        }

        String url = "https://data.xotelo.com/api/list" +
                "?location_key=" + locationKey +
                "&offset=0&limit=20";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Error en respuesta de Xotelo: " + response.code());
                return hotels;
            }

            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);

            JSONArray hotelArray = jsonObject.getJSONArray("hotels");

            for (int i = 0; i < hotelArray.length(); i++) {
                JSONObject hotel = hotelArray.getJSONObject(i);

                String id = hotel.getString("id");
                String name = hotel.optString("name", "Hotel sin nombre");
                String address = hotel.optString("address", "Dirección desconocida");
                String city = hotel.optString("city", cityName);
                double rating = hotel.optDouble("rating", 0.0);
                double lat = hotel.optDouble("lat", 0.0);
                double lon = hotel.optDouble("lon", 0.0);
                double price = hotel.optDouble("price", 0.0);
                String currency = hotel.optString("currency", "EUR");

                List<PriceOffer> offers = List.of(
                        new PriceOffer("Xotelo", price, currency)
                );

                hotels.add(new HotelData(
                        id, name, city, address, rating,
                        lat, lon, offers
                ));
            }

        } catch (IOException | JSONException e) {
            System.err.println("Error al procesar hoteles de Xotelo: " + e.getMessage());
        }

        return hotels;
    }
}
