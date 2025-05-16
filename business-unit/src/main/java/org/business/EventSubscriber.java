package org.business;

import com.google.gson.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.shared.HotelEvent;
import org.shared.EventInfo;
import org.shared.InstantTypeAdapter;

import javax.jms.*;
import java.time.Instant;

public class EventSubscriber {

    public static void startListening(Datamart datamart) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = factory.createConnection();
            connection.setClientID("BusinessUnit");
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Suscribirse a eventos de hoteles
            Topic hotelTopic = session.createTopic("HotelPrice");
            MessageConsumer hotelConsumer = session.createDurableSubscriber(hotelTopic, "BusinessSub-Hotel");

            // Suscribirse a eventos de Ticketmaster
            Topic eventTopic = session.createTopic("TicketmasterEvents");
            MessageConsumer eventConsumer = session.createDurableSubscriber(eventTopic, "BusinessSub-Event");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                    .create();

            // Listener para hoteles
            hotelConsumer.setMessageListener(message -> {
                if (message instanceof TextMessage text) {
                    try {
                        HotelEvent event = gson.fromJson(text.getText(), HotelEvent.class);
                        datamart.addEvent(event);
                        System.out.println("Hotel recibido: " + event.name);
                    } catch (Exception e) {
                        System.err.println("Error procesando HotelEvent: " + e.getMessage());
                    }
                }
            });

            // Listener para eventos
            eventConsumer.setMessageListener(message -> {
                if (message instanceof TextMessage text) {
                    try {
                        EventInfo event = gson.fromJson(text.getText(), EventInfo.class);
                        datamart.addEvent(event);
                        System.out.println("Evento recibido: " + event.name);
                    } catch (Exception e) {
                        System.err.println("Error procesando EventInfo: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Error al conectar con ActiveMQ: " + e.getMessage());
        }
    }
}
