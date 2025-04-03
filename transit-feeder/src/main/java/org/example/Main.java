package org.example;

public class Main {
    public static void main(String[] args) {

        String apiKey = args[0];
        new OpenRouteServiceController().run(apiKey);
    }
}
