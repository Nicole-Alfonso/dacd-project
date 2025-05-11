import com.google.gson.Gson;
import domain.model.HotelData;
import org.apache.activemq.ActiveMQConnectionFactory;
import application.HotelProvider;
import application.HotelStore;
import org.example.shared.HotelEvent;

import javax.jms.*;
import java.util.List;

public class XoteloController {

    private final HotelProvider provider;
    private final HotelStore store;

    public XoteloController(HotelProvider provider, HotelStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void fetchSaveAndPublish(String provinceApiKey) {
        List<HotelData> hotels = provider.fetchHotels(provinceApiKey);
        Gson gson = new Gson();

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("HotelPrice");
            MessageProducer producer = session.createProducer(destination);

            for (HotelData hotel : hotels) {
                // 1. Guardar en SQLite
                store.saveHotel(hotel);

                // 2. Crear y publicar evento
                HotelEvent event = new HotelEvent(
                        "Xotelo",
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getAddress(),
                        hotel.getCity(),
                        hotel.getProvince(),         // ¡Asegúrate de tener este getter!
                        hotel.getRating(),
                        hotel.getLatitude(),
                        hotel.getLongitude(),
                        hotel.getPriceOffers()
                );

                String json = gson.toJson(event);
                TextMessage message = session.createTextMessage(json);
                producer.send(message);
            }

            session.close();
            connection.close();

        } catch (JMSException e) {
            System.err.println("Error enviando evento a ActiveMQ: " + e.getMessage());
        }
    }
}