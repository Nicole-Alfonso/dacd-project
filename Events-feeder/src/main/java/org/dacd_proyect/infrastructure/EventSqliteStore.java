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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public EventSqliteStore(String dbUrl) {
        this.dbUrl = dbUrl;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl)) {
            String sql = "CREATE TABLE IF NOT EXISTS events ("
                    + "id TEXT PRIMARY KEY, "
                    + "name TEXT, "
                    + "location TEXT, "
                    + "date TEXT, "
                    + "url TEXT, "
                    + "latlong TEXT)";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveEvents(List<Event> events) throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl)) {
            String sql = "INSERT OR REPLACE INTO events (id, name, location, date, url, latlong) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (Event event : events) {
                System.out.println("Guardando evento: " + event.getName() + " en " + event.getLocation());
                pstmt.setString(1, event.getId());
                pstmt.setString(2, event.getName());
                pstmt.setString(3, event.getLocation());
                pstmt.setString(4, event.getDate().format(FORMATTER));  // Verifica que el formato de fecha es correcto
                pstmt.setString(5, event.getUrl());
                pstmt.setString(6, event.getLatlong());
                pstmt.addBatch();
            }

            int[] result = pstmt.executeBatch();
            System.out.println("Eventos guardados correctamente: " + result.length);
        } catch (Exception e) {
            System.err.println("Error al guardar eventos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    @Override
    public void saveEvents(List<Event> events) throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl)) {
            String sql = "INSERT OR REPLACE INTO events (id, name, location, date, url, latlong) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (Event event : events) {
                pstmt.setString(1, event.getId());
                pstmt.setString(2, event.getName());
                pstmt.setString(3, event.getLocation());
                pstmt.setString(4, event.getDate().format(FORMATTER));
                pstmt.setString(5, event.getUrl());
                pstmt.setString(6, event.getLatlong());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
*/

    @Override
    public List<Event> getAllEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl)) {
            String sql = "SELECT * FROM events";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Event event = new Event(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        LocalDateTime.parse(rs.getString("date"), FORMATTER),
                        rs.getString("url"),
                        rs.getString("latlong")
                );
                events.add(event);
            }

            System.out.println("Eventos existentes en la base de datos: " + events.size());  // Depuración
            return events;
        }
    }
}

/*
    @Override
    public List<Event> getAllEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl)) {
            String sql = "SELECT * FROM events";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Event event = new Event(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        LocalDateTime.parse(rs.getString("date"), FORMATTER),
                        rs.getString("url"),
                        rs.getString("latlong")
                );
                events.add(event);
            }
        }
        return events;
    }
}
*/
