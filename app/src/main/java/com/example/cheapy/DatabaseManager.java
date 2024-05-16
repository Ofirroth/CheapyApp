package com.example.cheapy;

import android.content.Context;

import androidx.room.Room;

import com.example.cheapy.Dao.AppDB;

public class DatabaseManager {

    private static AppDB instance;

    public static synchronized AppDB getDatabase() {
        if (instance == null) {
            instance = Room.databaseBuilder(Cheapy.context, AppDB.class, "cheapyDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
