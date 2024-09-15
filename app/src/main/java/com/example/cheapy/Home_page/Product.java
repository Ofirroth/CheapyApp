package com.example.cheapy.Home_page;

public class Product {
    private String name;
    private int imageResourceId;
    private int quantity;
    private double price;  // Change to double

    public Product(String name, int imageResourceId, double price) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.price = price;  // Ensure price is passed as a double
        this.quantity = 0; // Default quantity
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public double getPrice() {
        return price;  // Now returns a double
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
