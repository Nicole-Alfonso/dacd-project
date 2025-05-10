package org.dacd_proyect;

import org.dacd_proyect.infrastructure.TicketmasterProvider;

public class Main {
    public static void main(String[] args) {
        String apiKey = args[0];  // Reemplaza con tu API key
        String location = "Madrid";     // Ejemplo de ubicación
        String date = "2025-06-10";     // Ejemplo de fecha

        // Crear una instancia del proveedor de eventos
        TicketmasterProvider provider = new TicketmasterProvider(apiKey);

        // Llamar al método que obtiene y muestra los eventos
        provider.fetchAndPrintEvents(location, date);
    }
}
