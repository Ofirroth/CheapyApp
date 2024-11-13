package com.example.cheapy.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Price {
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    private String itemId;
    private double price;
    private String storeId;


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Price(String itemId, String storeId, double price) {
        this.itemId = itemId;
        this.price = price;
        this.storeId = storeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}