package com.example.cheapy.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Store {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String city;

    private String image;

    public Store(String name, String city, String image) {
        this.name = name;
        this.city = city;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}