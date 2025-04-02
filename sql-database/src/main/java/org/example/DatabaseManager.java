package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:transit_data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transit_routes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "data TEXT NOT NULL, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
            System.out.println("Tabla 'transit_routes' verificada/creada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    public static void insertData(String jsonData) {
        String sql = "INSERT INTO transit_routes (data) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jsonData);
            pstmt.executeUpdate();
            System.out.println("Datos guardados en la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al insertar datos: " + e.getMessage());
        }
    }
}