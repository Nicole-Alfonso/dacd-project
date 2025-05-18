package org.business;

import org.shared.FiltroHotel;

public class BusinessLauncher {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso:");
            System.out.println("  evento <nombreEvento> [categoria] [precioMax] [minRating] [distanciaMaxKm]");
            return;
        }

        String nombreEvento = args[0];
        String categoria = args.length >= 2 ? args[1] : null;
        double maxPrecio = args.length >= 3 ? Double.parseDouble(args[2]) : Double.MAX_VALUE;
        double minRating = args.length >= 4 ? Double.parseDouble(args[3]) : 0.0;
        double maxDistancia = args.length >= 5 ? Double.parseDouble(args[4]) : Double.MAX_VALUE;

        FiltroHotel filtro = new FiltroHotel(categoria, maxPrecio, minRating, maxDistancia);

        BusinessUnit unit = new BusinessUnit();
        unit.start();

        unit.getHotelesParaEvento(nombreEvento, filtro)
                .forEach(System.out::println);
    }
}

