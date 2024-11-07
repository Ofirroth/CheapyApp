package com.example.cheapy.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.repositories.CategoryRepository;
import com.example.cheapy.repositories.ItemRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private LiveData<List<Category>> allCategories;
    private CategoryRepository categoryRepository;

    public CategoryViewModel(String token) {
        this.categoryRepository = new CategoryRepository(token);
        this.allCategories = categoryRepository.getCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        reload();
        return this.allCategories;
    }

    public void reload() {
        this.allCategories = categoryRepository.getCategories();
    }
}