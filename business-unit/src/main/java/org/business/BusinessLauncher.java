package org.business;

public class BusinessLauncher {
    public static void main(String[] args) {
        BusinessUnit unit = new BusinessUnit();
        unit.start();

        if (args.length < 2) {
            System.out.println("Error en la llamada. Ejemplo de uso:");
            System.out.println("  evento <nombreEvento> <precioMax> [categoria] [minRating] [distanciaMaxKm]");
            return;
        }

        String nombreEvento = args[0];
        double maxPrecio = Double.parseDouble(args[1]);
        String categoria = args.length >= 3 ? args[2] : null;
        double minRating = args.length >= 4 ? Double.parseDouble(args[3]) : 0.0;
        double maxDistancia = args.length >= 5 ? Double.parseDouble(args[4]) : Double.MAX_VALUE;

        unit.getHotelesParaEvento(nombreEvento, maxPrecio, categoria, minRating, maxDistancia)
                .forEach(System.out::println);
    }
}
