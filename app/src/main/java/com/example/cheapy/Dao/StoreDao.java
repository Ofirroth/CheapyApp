package com.example.cheapy.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cheapy.entities.Store;

import java.util.List;

@Dao
public interface StoreDao {
    @Query("SELECT * FROM Store")
    List<Store> getItems();
    @Query("SELECT * FROM Store ORDER BY id")
    List<Store> getAllStores();

    @Insert
    void insert(Store... stores);

    @Update
    void update(Store... stores);

    @Delete
    void delete(Store... stores);
}
