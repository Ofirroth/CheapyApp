package com.example.cheapy.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;
import androidx.room.PrimaryKey;

import com.example.cheapy.converts.itemConverter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
@TypeConverters(itemConverter.class)
public class Item implements Serializable {
    @SerializedName("categoryId")
    private int categoryId;

    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    private final String name;
    @SerializedName("itemPic")
    private final String imageResource;
    private String category;
    private int quantity;
    private double price;

    public Item(String name, String imageResource, String category, int quantity) {
        this.name = name;
        this.imageResource = imageResource;
        this.category = category;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
