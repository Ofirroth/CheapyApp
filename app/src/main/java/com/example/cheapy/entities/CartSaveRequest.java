package com.example.cheapy.entities;

import java.util.List;

public class CartSaveRequest {
    private String storeId;
    private double totalPrice;
    private String date;
    private List<Item> items;
    private String userName;


    public CartSaveRequest(String storeId, double totalPrice, String date, List<Item> items, String userName) {
        this.storeId = storeId;
        this.totalPrice = totalPrice;
        this.date = date;
        this.items = items;
        this.userName = userName;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
