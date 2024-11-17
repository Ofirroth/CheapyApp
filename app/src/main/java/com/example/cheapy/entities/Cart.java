package com.example.cheapy.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.cheapy.converts.ItemListConverter;
import com.example.cheapy.converts.cartConverter;

import java.util.List;

@Entity
@TypeConverters(ItemListConverter.class)
public class Cart {
    private String userId;
    private String storeId;
    private double totalPrice;
    private List<Item> items;
    @PrimaryKey
    @NonNull
    private String dateCreated;

    public Cart(String userId, String storeId, double totalPrice, List<Item> items, String dateCreated) {
        this.userId = userId;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
        this.items = items;
        this.dateCreated = dateCreated;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
