package org.feeder;

import com.google.gson.*;
import java.time.Instant;
import org.feeder.application.HotelProvider;
import org.feeder.application.HotelStore;
import org.feeder.model.HotelData;
import org.shared.HotelEvent;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.shared.InstantTypeAdapter;

import java.util.List;

public class XoteloController {
    private final HotelProvider provider;
    private final HotelStore store;

    public XoteloController(HotelProvider provider, HotelStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void fetchSaveAndPublish(String cityApiKey, String cityName) {
        List<HotelData> hotels = provider.fetchHotels(cityApiKey);
        System.out.println("Hoteles obtenidos: " + hotels.size());

        try (
                Connection connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
        ) {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(session.createTopic("HotelPrice"));

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                    .create();

            for (HotelData hotel : hotels) {
                try {
                    store.saveHotel(hotel);
                    HotelEvent event = new HotelEvent(
                            Instant.now(), "Xotelo",
                            hotel.getId(), hotel.getName(), hotel.getCity_code() ,hotel.getCity(),
                            hotel.getRating(), hotel.getLatitude(), hotel.getLongitude(),
                            hotel.getMinPrice(), hotel.getMaxPrice(),
                            hotel.getCategory().name(), hotel.getPriceOffers(), hotel.getUrl()
                    );

                    String json = gson.toJson(event);
                    producer.send(session.createTextMessage(json));
                    System.out.println("Evento enviado: " + hotel.getName());

                } catch (Exception ex) {
                    System.err.println("Error con hotel " + hotel.getName() + ": " + ex.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Error con JMS: " + e.getMessage());
        }
    }
}
