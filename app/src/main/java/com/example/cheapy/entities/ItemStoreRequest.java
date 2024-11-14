package com.example.cheapy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class ItemStoreRequest {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String storeId;
    private String itemId;

    public ItemStoreRequest(String storeId, String itemId) {
        this.storeId = storeId;
        this.itemId = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
