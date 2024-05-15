package com.example.cheapy.converts;

import androidx.room.TypeConverter;

import com.example.cheapy.entities.User;
import com.google.gson.Gson;

public class userConvert {
    @TypeConverter
    public static User fromJsonString(String value) {
        return new Gson().fromJson(value, User.class);
    }

    @TypeConverter
    public static String toJsonString(User user) {
        return new Gson().toJson(user);
    }
}
