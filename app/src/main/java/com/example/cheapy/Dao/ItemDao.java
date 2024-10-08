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
    @Query("SELECT * FROM Item")
    //getmessages
    List<Item> getItems();
    @Query("SELECT * FROM Item WHERE category = :category ORDER BY id")
    //getallmessageswithcontact
    LiveData<List<Item>> getAllItemsFromCategory(String category);

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item... items);
}
