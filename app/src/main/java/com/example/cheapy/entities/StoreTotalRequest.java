package com.example.cheapy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.cheapy.converts.ItemListConverter;
import java.util.List;

@Entity
@TypeConverters(ItemListConverter.class)
public class StoreTotalRequest {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String storeName;
    private List<Item> items;

    public StoreTotalRequest(String storeName, List<Item> items) {
        this.storeName = storeName;
        this.items = items;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
