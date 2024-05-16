package com.example.cheapy.converts;

import androidx.room.TypeConverter;

import com.example.cheapy.entities.Item;
import com.google.gson.Gson;

public class itemConverter {
    @TypeConverter
    public static Item fromJsonString(String value) {
        return new Gson().fromJson(value, Item.class);
    }

    @TypeConverter
    public static String toJsonString(Item item) {
        return new Gson().toJson(item);
    }
}
