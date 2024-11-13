package com.example.cheapy.Dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cheapy.converts.stringListConverter;
import com.example.cheapy.converts.categoryConverter;
import com.example.cheapy.converts.storeConverter;
import com.example.cheapy.converts.subCategoryConverter;
import com.example.cheapy.entities.Category;
import com.example.cheapy.converts.itemConverter;
import com.example.cheapy.converts.userConvert;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Price;
import com.example.cheapy.entities.Store;
import com.example.cheapy.entities.StoreTotalRequest;
import com.example.cheapy.entities.SubCategory;

@Database(entities = {Category.class, Item.class, Store.class, SubCategory.class,StoreTotalRequest.class, Price.class}, version = 10)
@TypeConverters({userConvert.class, itemConverter.class, storeConverter.class, categoryConverter.class, subCategoryConverter.class, stringListConverter.class})
 public abstract class AppDB extends RoomDatabase {
    public static final String DATABASE_NAME = "27018ChatDB.db";
     public abstract ItemDao itemDao();
    public abstract StoreDao storeDao();
    public abstract PriceDao priceDao();
    public abstract CategoryDao categoryDao();

}
