package com.example.cheapy.viewModels;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.cheapy.entities.SubCategory;
import com.example.cheapy.repositories.SubCategoryRepository;

import java.util.List;

public class SubCategoryViewModel extends ViewModel {
    private SubCategoryRepository repository;
    private LiveData<List<SubCategory>> subcategories;
    private int categoryId;

    public SubCategoryViewModel(String token, int categoryId) {
        repository = new SubCategoryRepository(token, categoryId);
        subcategories = repository.getSubcategoriesByCategoryId();
        this.categoryId = categoryId;
    }

    public LiveData<List<SubCategory>> getSubcategoriesByCategoryId() {
        reload();
        return subcategories;
    }

    public void reload() {
        this.subcategories = repository.getSubcategoriesByCategoryId(); }
}
