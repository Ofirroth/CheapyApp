package com.example.cheapy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.cheapy.converts.itemConverter;

import java.util.Map;

@Entity
@TypeConverters(itemConverter.class)
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int price;

    private int category;
    /*
    // True if the message was sent by the current user
    private Map<String,String> sender;
    // The contact id of the user the current user is chatting with
    private int contactId;
    */
    public Item(String name, int price, int category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
