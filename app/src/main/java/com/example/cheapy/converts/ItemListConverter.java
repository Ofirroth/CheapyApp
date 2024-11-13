package com.example.cheapy.converts;

import androidx.room.TypeConverter;
import com.example.cheapy.entities.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class ItemListConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromItemList(List<Item> items) {
        return gson.toJson(items);
    }

    @TypeConverter
    public static List<Item> toItemList(String itemsJson) {
        Type listType = new TypeToken<List<Item>>() {}.getType();
        return gson.fromJson(itemsJson, listType);
    }
}
