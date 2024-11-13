package com.example.cheapy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SubCategory {

    @PrimaryKey
    private int id;
    private String name;
    private String image;
    private int parentId;

    public SubCategory(int id, String name, String image, int parentId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.parentId = parentId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
