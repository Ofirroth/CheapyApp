package com.example.cheapy.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.SubCategory;

import java.util.List;
@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> getCategories();
    @Query("SELECT * FROM Category WHERE id = :customId LIMIT 1")
    LiveData<Category> getCategoryByCustomId(int customId);

    @Query("SELECT * FROM SubCategory WHERE parentId = :categoryId")
    List<SubCategory> getSubCategories(int categoryId);
    @Insert
    void insert(Category... categories);
    @Update
    void update(Category... categories);
    @Delete
    void delete(Category... categories);
}
