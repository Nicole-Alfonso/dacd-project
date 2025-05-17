package org.dacd_proyect.infrastructure;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.domain.model.Event;

import java.sql.*;
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
                        ss TEXT,
                        id TEXT,
                        name TEXT PRIMARY KEY,
                        keyword TEXT,
                        city TEXT,
                        country_code TEXT,
                        ts TEXT,
                        date TEXT,
                        url TEXT,
                        lat REAL,
                        lon REAL
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
                             "ss, id, name, keyword, city, country_code, " +
                             "ts, date, url, lat, lon) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
             );
        ) {

            pstmt.setString(1, event.getSource());
            pstmt.setString(2, event.getId());
            pstmt.setString(3, event.getName());
            pstmt.setString(4, event.getKeyword());
            pstmt.setString(5, String.join(",", event.getCity()));
            pstmt.setString(6, event.getCountryCode());
            pstmt.setString(7, event.getTimestamp().toString());
            pstmt.setString(8, event.getStartDateTime());
            pstmt.setString(9, event.getUrl());
            pstmt.setDouble(10, event.getLat());
            pstmt.setDouble(11, event.getLon());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error guardando evento en la base de datos: " + e.getMessage());
        }
    }
}
