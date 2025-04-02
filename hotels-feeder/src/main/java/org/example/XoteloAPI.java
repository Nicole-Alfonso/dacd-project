package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;

public class XoteloAPI {
    public static void main(String[] args) {

        // Verificamos que se haya pasado la API Key como argumento
        if (args.length == 0) {
            System.err.println("Se debe proporcionar la clave de API como argumento.");
            return;
        }

        // Obtenemos la clave de API desde los argumentos
        String apiKey = args[0];

        try {
            // La URL de la API de Xotelo, usando la clave de API
            String endpoint = "https://data.xotelo.com/api/list?location_key=" + apiKey + "&offset=0&limit=5";

            // Realizamos la conexión y obtenemos la respuesta
            Connection connection = Jsoup.connect(endpoint)
                    .ignoreContentType(true) // Ignorar el tipo de contenido
                    .header("Content-Type", "application/json") // Cabecera de tipo de contenido
                    .method(Connection.Method.GET); // Metodo GET para la petición

            // Ejecutamos la solicitud y obtenemos el cuerpo de la respuesta
            String jsonResponse = connection.execute().body();

            // Imprimimos la respuesta de la API
            System.out.println("Respuesta de Xotelo API:");
            System.out.println(jsonResponse);

        } catch (IOException e) {
            // Si ocurre un error, lo mostramos
            System.err.println("Error al conectar con la API de Xotelo: " + e.getMessage());
        }
    }
}
