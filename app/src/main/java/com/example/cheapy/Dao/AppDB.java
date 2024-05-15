package com.example.cheapy.Dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.converts.itemConverter;
import com.example.cheapy.converts.userConvert;

@Database(entities = {Category.class, Item.class}, version = 1)
@TypeConverters({userConvert.class, itemConverter.class})
 public abstract class AppDB extends RoomDatabase {
    public static final String DATABASE_NAME = "27018ChatDB.db";
     public abstract com.example.cheapy.Dao.ContactDao contactDao();
     public abstract com.example.cheapy.Dao.ItemDao itemDao();

}
