package org.business;

import org.business.events.EventStoreLoader;
import org.business.events.LiveEventSubscriber;
import org.shared.HotelFilter;

public class BusinessLauncher {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso:");
            System.out.println("  evento <nombreEvento> [categoria] [precioMax] [minRating] [distanciaMaxKm]");
            return;
        }

        // Parsear argumentos
        String nombreEvento = args[0];
        String categoria = args.length >= 2 ? args[1] : null;
        double maxPrecio = args.length >= 3 ? Double.parseDouble(args[2]) : Double.MAX_VALUE;
        double minRating = args.length >= 4 ? Double.parseDouble(args[3]) : 0.0;
        double maxDistancia = args.length >= 5 ? Double.parseDouble(args[4]) : Double.MAX_VALUE;

        HotelFilter filtro = new HotelFilter(categoria, maxPrecio, minRating, maxDistancia);

        // Inicialización completa
        Datamart datamart = new Datamart();

        // 1. Cargar históricos
        EventStoreLoader.loadAllEvents(datamart);

        // 2. Suscribirse a eventos en tiempo real
        LiveEventSubscriber.startListening(datamart);

        // 3. Ejecutar lógica de negocio
        BusinessUnit unit = new BusinessUnit(datamart);
    }
}
