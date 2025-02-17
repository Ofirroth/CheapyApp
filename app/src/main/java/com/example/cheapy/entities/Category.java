package com.example.cheapy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity
public class Category {

    @PrimaryKey
    private int id;
    private String name;
    private String image;
    private boolean isExp = false;

    public Category(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public boolean isExp() {
        return isExp;
    }

    public void setExp(boolean exp) {
        isExp = exp;
    }
}