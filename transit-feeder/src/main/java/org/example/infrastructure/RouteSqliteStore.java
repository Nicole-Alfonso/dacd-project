package org.example.infrastructure;

import org.example.application.RouteStore;
import org.example.domain.model.Route;

import java.sql.*;

public class RouteSqliteStore implements RouteStore {
    private static final String URL = "jdbc:sqlite:openrouteservice.db";

    public RouteSqliteStore() {
        createTable();
    }

    @Override
    public void save(Route route) {
//TODO
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public  void createTable() {
        String sqlCreateTable = """
            CREATE TABLE IF NOT EXISTS api_responses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                coordinates TEXT NOT NULL,
                response_json TEXT NOT NULL
            );
        """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCreateTable);
            System.out.println("Tabla 'api_responses' creada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    public static void insertApiResponse(Connection connection, String jsonResponse, String coordinates) throws SQLException {
        String sqlInsert = "INSERT INTO api_responses (coordinates, response_json) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sqlInsert)) {
            stmt.setString(1, coordinates);
            stmt.setString(2, jsonResponse);
            stmt.executeUpdate();
            System.out.println("Datos guardados en la base de datos.");
        }
    }

    public boolean isDuplicate(Connection connection, String coordinates) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM api_responses WHERE coordinates = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlCheck)) {
            stmt.setString(1, coordinates);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}


