package org.business.events;

import com.google.gson.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.business.Datamart;
import org.shared.HotelEvent;
import org.shared.EventInfo;
import org.shared.InstantTypeAdapter;
import org.shared.LocalDateTypeAdapter;

import javax.jms.*;
import java.time.Instant;
import java.time.LocalDate;

public class LiveEventSubscriber {

    public static void startListening(Datamart datamart) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = factory.createConnection();
            connection.setClientID("BusinessUnit");
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic hotelTopic = session.createTopic("HotelPrice");
            Topic concertTopic = session.createTopic("TicketmasterEvents");

            MessageConsumer hotelConsumer = session.createDurableSubscriber(hotelTopic, "BusinessSub-Hotel");
            MessageConsumer concertConsumer = session.createDurableSubscriber(concertTopic, "BusinessSub-Event");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();

            hotelConsumer.setMessageListener(msg -> {
                if (msg instanceof TextMessage text) {
                    try {
                        HotelEvent hotelEvent = gson.fromJson(text.getText(), HotelEvent.class);
                        datamart.addHotel(hotelEvent);
                        System.out.println("HotelEvent recibido: " + hotelEvent.getName());
                    } catch (Exception e) {
                        System.err.println("Error HotelEvent: " + e.getMessage());
                    }
                }
            });

            concertConsumer.setMessageListener(msg -> {
                if (msg instanceof TextMessage text) {
                    try {
                        EventInfo concert = gson.fromJson(text.getText(), EventInfo.class);
                        datamart.addEvent(concert);
                        System.out.println("📩 EventInfo recibido: " + concert.getName());
                    } catch (Exception e) {
                        System.err.println("Error EventInfo: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Error conectando a ActiveMQ: " + e.getMessage());
        }
    }
}
