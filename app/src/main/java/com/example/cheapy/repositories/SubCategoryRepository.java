package com.example.cheapy.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.API.CategoryAPI;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.SubCategory;

import java.util.List;

public class SubCategoryRepository {
    private CategoryDao categoryDao;
    private SubListData subListData;
    private AppDB db;
    private CategoryAPI categoryAPI;
    private String token;
    private int categoryId;

    public SubCategoryRepository(String token, int categoryId) {
        this.token = token;
        this.db = DatabaseManager.getDatabase();
        this.categoryDao = db.categoryDao();
        this.subListData = new SubListData();
        this.categoryAPI = new CategoryAPI();
        this.categoryId = categoryId;

    }

    public LiveData<List<SubCategory>> getSubcategoriesByCategoryId() {
        reload();
        return subListData;
    }

    public void reload() {
        CategoryAPI categoryAPI = new CategoryAPI();
        categoryAPI.fetchSubcategoriesFromCategory(token, categoryId, subListData);
        categoryDao.delete();
        List<SubCategory> categoryList = subListData.getValue();
        subListData.postValue(categoryList);
    }

    class SubListData extends MutableLiveData<List<SubCategory>> {
        public SubListData() {
            super();
            setValue(categoryDao.getSubCategories(categoryId));
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                categoryAPI.fetchSubcategoriesFromCategory(token, categoryId, subListData);
            }).start();
        }
    }
}

