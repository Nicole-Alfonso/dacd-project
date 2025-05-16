
package org.dacd_proyect.infrastructure;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.domain.model.Event;

import java.sql.*;
import java.time.Instant;
import java.util.List;

public class EventSqliteStore implements EventStore {
    private final String url;

    public EventSqliteStore(String url) {
        this.url = url;
        initDatabase();
    }

    private void initDatabase() {
        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {

            String sql = """
                    CREATE TABLE IF NOT EXISTS events (
                        id TEXT PRIMARY KEY,
                        name TEXT,
                        keyword TEXT,
                        venue_id TEXT,
                        city TEXT,
                        country_code TEXT,
                        timestamp TEXT,
                        start_date_time TEXT,
                        end_date_time TEXT,
                        url TEXT,
                        latlong TEXT
                    );
                    """;

            stmt.executeUpdate(sql);
            System.out.println("Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
        }
    }

    @Override
    public void saveEvent(Event event) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT OR REPLACE INTO events (" +
                             "id, name, keyword, venue_id, city, country_code, " +
                             "timestamp, start_date_time, end_date_time, url, latlong) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
             );
        ) {

            pstmt.setString(1, event.getId());
            pstmt.setString(2, event.getName());
            pstmt.setString(3, event.getKeyword());
            pstmt.setString(4, event.getVenueId());
            pstmt.setString(5, String.join(",", event.getCity()));
            pstmt.setString(6, event.getCountryCode());
            pstmt.setString(7, event.getTimestamp().toString());
            pstmt.setString(8, event.getStartDateTime());
            pstmt.setString(9, event.getEndDateTime());
            pstmt.setString(10, event.getUrl());
            pstmt.setString(11, event.getLatlong());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error guardando evento en la base de datos: " + e.getMessage());
        }
    }
}




