package com.example.cheapy.converts;

import androidx.room.TypeConverter;

import com.example.cheapy.entities.Store;
import com.google.gson.Gson;

public class storeConverter {
    @TypeConverter
    public static Store fromJsonString(String value) {
        return new Gson().fromJson(value, Store.class);
    }

    @TypeConverter
    public static String toJsonString(Store store) {
        return new Gson().toJson(store);
    }
}
