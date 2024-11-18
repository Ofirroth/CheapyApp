package com.example.cheapy.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class shoppingListHistoryItem {
    @NonNull
    @PrimaryKey
    @SerializedName("cartId")
    private String id;
    private String storeId;
    private String userId;
    private String storeName;
    private String date;
    private String storeCity;
    private String storeImage;
    private Double totalPrice;

    public shoppingListHistoryItem(String storeName, String date, String storeCity, String storeImage, Double totalPrice) {
        this.storeName = storeName;
        this.date = date;
        this.storeCity = storeCity;
        this.storeImage = storeImage;
        this.totalPrice = totalPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
