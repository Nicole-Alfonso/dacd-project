import com.google.gson.Gson;
import domain.model.Ciudades;
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
                // Guardar en SQLite
                store.saveHotel(hotel);

                // Obtener el nombre de la ciudad desde el key
                String ciudadNombre = Ciudades.getNombreCiudadPorKey(hotel.getCity());

                if (ciudadNombre == null) {
                    System.err.println("No se encontró clave para la ciudad: " + hotel.getCity());
                    continue;
                }

                // Crear y publicar evento
                HotelEvent event = new HotelEvent(
                        "Xotelo",
                        hotel.getId(),
                        hotel.getName(),
                        ciudadNombre,
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
