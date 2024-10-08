package com.example.cheapy.entities;
import androidx.room.Entity;
import androidx.room.TypeConverters;
import androidx.room.PrimaryKey;

import com.example.cheapy.converts.itemConverter;

@Entity
@TypeConverters(itemConverter.class)
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String name;
    private final String imageResource;
    private double price;
    private Store store;
    private String category;
    private int quantity;

    public Item(String name, String imageResource, double price, Store store, String category, int quantity) {
        this.name = name;
        this.imageResource = imageResource;
        this.price = price;
        this.store = store;
        this.category = category;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getImageResource() {
        return imageResource;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {  // Setter for price
        this.price = price;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
