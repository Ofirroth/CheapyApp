package com.example.cheapy.converts;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;

public class stringListConverter {

    @TypeConverter
    public String fromStringList(List<String> itemIds) {
        return itemIds != null ? String.join(",", itemIds) : "";
    }

    @TypeConverter
    public List<String> toStringList(String data) {
        return data.isEmpty() ? null : Arrays.asList(data.split(","));
    }
}
