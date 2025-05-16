package org.business;

import java.util.Optional;

public class BusinessLauncher {
    public static void main(String[] args) {
        BusinessUnit unit = new BusinessUnit();
        unit.start();

        if (args.length == 0) {
            System.out.println("LLamada incorrecta:");
            System.out.println("Ejemplo de Uso:");
            System.out.println("  evento <nombreEvento> <precioMax> [categoria] [minRating] [maxDistKm]");
            return;
        }

        String comando = args[0];

        if (comando.equalsIgnoreCase("baratos") && args.length == 3) {
            String ciudad = args[1];
            double precio = Double.parseDouble(args[2]);
            unit.getBaratos(ciudad, precio).forEach(System.out::println);

        } else if (comando.equalsIgnoreCase("top") && args.length == 3) {
            String ciudad = args[1];
            int topN = Integer.parseInt(args[2]);
            unit.getTopValorados(ciudad, topN).forEach(System.out::println);

        } else if (comando.equalsIgnoreCase("categoria") && args.length == 3) {
            String ciudad = args[1];
            String categoria = args[2]; // LOW, MEDIUM, HIGH
            unit.getHotelesPorCategoria(ciudad, categoria).forEach(System.out::println);

        } else if (comando.equalsIgnoreCase("evento") && args.length >= 3) {
            String nombreEvento = args[1];
            double precioMax = Double.parseDouble(args[2]);
            Optional<String> categoria = (args.length >= 4) ? Optional.of(args[3]) : Optional.empty();
            Optional<Double> minRating = (args.length >= 5) ? Optional.of(Double.parseDouble(args[4])) : Optional.empty();
            Optional<Double> maxDist = (args.length >= 6) ? Optional.of(Double.parseDouble(args[5])) : Optional.empty();

            unit.getHotelesFiltradosParaEvento(nombreEvento, categoria, Optional.of(precioMax), minRating, maxDist)
                    .forEach(System.out::println);
        } else {
            System.out.println("Comando no reconocido.");
        }
    }
}
