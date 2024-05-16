package com.example.cheapy.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cheapy.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    //getmessages
    List<Item> getItems();

    @Query("SELECT * FROM item WHERE category = :id ORDER BY id")
    //getallmessageswithcontact
    LiveData<List<Item>> getAllItemsFromCategory(int id);

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item... items);
}
