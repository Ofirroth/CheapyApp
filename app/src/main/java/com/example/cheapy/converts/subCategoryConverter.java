package com.example.cheapy.converts;

import androidx.room.TypeConverter;

import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.SubCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class subCategoryConverter {
    @TypeConverter
    public static List<SubCategory> fromJsonString(String value) {
        // Convert the stored string into a List<String> using Gson
        Type listType = new TypeToken<List<SubCategory>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJsonString(List<SubCategory> list) {
        // Convert the List<String> into a string using Gson
        return new Gson().toJson(list);
    }
}
