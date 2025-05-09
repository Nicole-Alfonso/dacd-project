package domain.model;

public class PriceOffer {
    private final String provider;
    private final double price;
    private final String currency;

    public PriceOffer(String provider, double price, String currency) {
        this.provider = provider;
        this.price = price;
        this.currency = currency;
    }

    public String getProvider() {return provider; }
    public double getPrice() {return price; }
    public String getCurrency() {return currency; }

    @Override
    public String toString() {
        return String.format("%s: %.2f %s", provider, price, currency);
    }
}

