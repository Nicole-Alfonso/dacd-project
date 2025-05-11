package org.dacd_proyect.infrastructure;

import org.dacd_proyect.application.EventStore;
import org.dacd_proyect.domain.model.Event;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



public class EventSqliteStore implements EventStore {
    private final String dbUrl;

    public EventSqliteStore(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS events (" +
                    "id TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "location TEXT, " +
                    "date TEXT, " +
                    "url TEXT, " +
                    "latlong TEXT)");

        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void saveEvent(Event event) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);

            String upsert = "INSERT OR REPLACE INTO events (id, name, location, date, url, latlong) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(upsert)) {
                stmt.setString(1, event.getId());
                stmt.setString(2, event.getName());
                stmt.setString(3, event.getLocation());
                stmt.setString(4, event.getDate()); // 👈 Date como String
                stmt.setString(5, event.getUrl());
                stmt.setString(6, event.getLatlong());
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error saving event: " + e.getMessage());
        }
    }
}






