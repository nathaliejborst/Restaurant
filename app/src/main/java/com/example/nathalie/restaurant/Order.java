package com.example.nathalie.restaurant;

/**
 * Created by Nathalie on 18-11-2017.
 */

public class Order {
    private int amount;
    private String name;
    private int priceTotal;

    public Order(int amount, String name, int priceTotal) {
        this.amount = amount;
        this.name = name;
        this.priceTotal = priceTotal;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(int priceTotal) {
        this.priceTotal = priceTotal;
    }
}
