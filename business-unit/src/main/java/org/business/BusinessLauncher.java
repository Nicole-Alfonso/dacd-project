package org.business;

public class BusinessLauncher {
    public static void main(String[] args) {
        BusinessUnit unit = new BusinessUnit();
        unit.start();

        if (args.length == 0) {
            System.out.println("Uso:");
            System.out.println("  baratos <provincia> <precioMax>");
            System.out.println("  top <provincia> <N>");
            return;
        }

        String comando = args[0];

        if (comando.equalsIgnoreCase("baratos") && args.length == 3) {
            String provincia = args[1];
            double precio = Double.parseDouble(args[2]);
            unit.getBaratos(provincia, precio).forEach(System.out::println);
        } else if (comando.equalsIgnoreCase("top") && args.length == 3) {
            String provincia = args[1];
            int topN = Integer.parseInt(args[2]);
            unit.getTopValorados(provincia, topN).forEach(System.out::println);
        } else {
            System.out.println("Comando no reconocido.");
        }
    }
}
