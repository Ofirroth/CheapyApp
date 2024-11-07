package com.example.cheapy.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.API.CategoryAPI;
import com.example.cheapy.API.ItemAPI;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private CategoryListData categoryListData;
    private CategoryAPI categoryAPI;
    private String token;
    private AppDB db;

    public CategoryRepository(String token) {
        this.token = token;
        this.db = DatabaseManager.getDatabase();
        this.categoryDao = db.categoryDao();
        this.categoryListData = new CategoryListData();
        this.categoryAPI = new CategoryAPI();
    }

    public LiveData<List<Category>> getCategories() {
        reload();
        return categoryListData;
    }

    public void reload() {
        CategoryAPI categoryAPI = new CategoryAPI();
        categoryAPI.getCategories(categoryListData, token);
        categoryDao.delete();
        List<Category> categoryList = categoryListData.getValue();
        categoryListData.postValue(categoryList);
    }

    class CategoryListData extends MutableLiveData<List<Category>> {
        public CategoryListData() {
            super();
            setValue(categoryDao.getCategories());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                categoryAPI.getCategories(this, token);
            }).start();
        }
    }
}
