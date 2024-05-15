package com.example.ap2_speakeasy.converts;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class senderConverter {
    @TypeConverter
    public static Map<String, String> fromString(String value) {
        Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
        return new Gson().fromJson(value, mapType);
    }

    @TypeConverter
    public static String toString(Map<String, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
