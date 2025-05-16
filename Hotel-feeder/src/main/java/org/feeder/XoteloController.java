package org.feeder;

import com.google.gson.*;
import java.time.Instant;
import org.feeder.application.HotelProvider;
import org.feeder.application.HotelStore;
import org.feeder.model.HotelData;
import org.shared.HotelEvent;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
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

        Connection connection = null;
        Session session = null;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("HotelPrice");
            MessageProducer producer = session.createProducer(destination);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new JsonSerializer<Instant>() {
                        @Override
                        public JsonElement serialize(Instant src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.toString()); // ISO-8601 string
                        }
                    })
                    .create();

            for (HotelData hotel : hotels) {
                try {
                    System.out.println("➡️ Guardando hotel con ID: " + hotel.getId());
                    store.saveHotel(hotel);

                    HotelEvent event = new HotelEvent(
                            Instant.now(),
                            "Xotelo",
                            hotel.getId(),
                            hotel.getName(),
                            hotel.getCity(),
                            hotel.getRating(),
                            hotel.getLatitude(),
                            hotel.getLongitude(),
                            hotel.getMinPrice(),
                            hotel.getMaxPrice(),
                            hotel.getCategory().name(),
                            hotel.getPriceOffers()
                    );

                    String json = gson.toJson(event);
                    System.out.println("➡ Enviando evento: " + json);
                    TextMessage message = session.createTextMessage(json);
                    producer.send(message);
                } catch (Exception ex) {
                    System.err.println("❌ Error con hotel ID " + hotel.getId() + ": " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

        } catch (JMSException e) {
            System.err.println("Error enviando evento a ActiveMQ: " + e.getMessage());
        } finally {
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                System.err.println("Error cerrando conexión JMS: " + e.getMessage());
            }
        }
    }
}