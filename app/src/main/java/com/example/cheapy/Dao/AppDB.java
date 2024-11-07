package com.example.cheapy.Dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cheapy.converts.categoryConverter;
import com.example.cheapy.converts.storeConverter;
import com.example.cheapy.entities.Category;
import com.example.cheapy.converts.itemConverter;
import com.example.cheapy.converts.userConvert;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;

@Database(entities = {Category.class, Item.class, Store.class}, version = 3)
@TypeConverters({userConvert.class, itemConverter.class, storeConverter.class, categoryConverter.class})
 public abstract class AppDB extends RoomDatabase {
    public static final String DATABASE_NAME = "27018ChatDB.db";
     public abstract com.example.cheapy.Dao.ContactDao contactDao();
     public abstract ItemDao itemDao();
    public abstract CategoryDao categoryDao();

}
