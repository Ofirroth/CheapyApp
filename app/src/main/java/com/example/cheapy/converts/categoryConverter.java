package com.example.cheapy.converts;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class categoryConverter {

    @TypeConverter
    public static List<String> fromJsonString(String value) {
        // Convert the stored string into a List<String> using Gson
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJsonString(List<String> list) {
        // Convert the List<String> into a string using Gson
        return new Gson().toJson(list);
    }
}
