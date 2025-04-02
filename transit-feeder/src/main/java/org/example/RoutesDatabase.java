package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RoutesDatabase {

    private static final String DB_URL = "jdbc:sqlite:openrouteservice.db"; // Ruta de la base de datos SQLite

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS rutas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "inicio TEXT, " +
                "fin TEXT, " +
                "distancia REAL, " +
                "duracion REAL)";
        try (Connection connection = connect(); Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Tabla 'rutas' creada o ya existe.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    public static void saveRoute(Route route) {
        String insertSQL = "INSERT INTO rutas (inicio, fin, distancia, duracion) VALUES (?, ?, ?, ?)";
        try (Connection connection = connect(); Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(String.format(insertSQL, route.getStart(), route.getEnd(), route.getDistance(), route.getDuration()));
            System.out.println("Ruta guardada en la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al guardar la ruta: " + e.getMessage());
        }
    }
}


