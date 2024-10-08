package com.example.cheapy.entities;

public class Product {
    private final String name;
    private final int imageResourceId;
    private int quantity;
    private double price;

    public Product(String name, int imageResourceId, double price) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.price = price;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {  // Setter for price
        this.price = price;
    }
}
