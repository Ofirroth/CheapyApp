package com.example.cheapy.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cheapy.entities.Category;

import java.util.List;
@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> getCategories();
    @Query("SELECT * FROM Category WHERE name = :category ORDER BY id")
    LiveData<List<Category>> getSubCategory(String category);
    @Insert
    void insert(Category... categories);
    @Update
    void update(Category... categories);
    @Delete
    void delete(Category... categories);
}
