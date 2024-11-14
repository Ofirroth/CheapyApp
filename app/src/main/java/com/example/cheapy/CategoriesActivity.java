package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Dao;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.Dao.ItemDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.Home_page.HomePageActivity;
import com.example.cheapy.R;
import com.example.cheapy.adapters.CategoryAdapter;
import com.example.cheapy.adapters.ItemAdapter;
import com.example.cheapy.databinding.HomePageBinding;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.viewModels.CategoryViewModel;
import com.example.cheapy.viewModels.ItemViewModel;
import com.example.cheapy.databinding.ActivityCategoriesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoriesActivity extends AppCompatActivity implements TextWatcher {

    String activeUserName;
    private ActivityCategoriesBinding binding;

    String userToken;

    private Boolean isNightMode = null;
    private AppDB db;
    private CategoryDao categoryDao;
    private CategoryAdapter adapter;

    private CategoryViewModel viewModel;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationMyProfile) {
                Intent profileIntent = new Intent(CategoriesActivity.this, profilePageActivity.class);
                profileIntent.putExtra("activeUserName", activeUserName);
                profileIntent.putExtra("token", userToken);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.navigationHome) {
                Intent homeIntent = new Intent(CategoriesActivity.this, HomePageActivity.class);
                homeIntent.putExtra("activeUserName", activeUserName);
                homeIntent.putExtra("token", userToken);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(CategoriesActivity.this, CartActivity.class);
                cartIntent.putExtra("activeUserName", activeUserName);
                cartIntent.putExtra("token", userToken);
                startActivity(cartIntent);
                return true;
            } else {
                return false;
            }
        });

        this.db = DatabaseManager.getDatabase();
        this.categoryDao = db.categoryDao();
        this.viewModel = new CategoryViewModel(this.userToken);
        this.categories = new ArrayList<>();
        binding.searchEditText.addTextChangedListener(this);
        binding.searchEditText.addTextChangedListener(this);
        GridView lvCategories = binding.gridViewCategories;
        this.adapter = new CategoryAdapter(getApplicationContext(), this.categories);
        this.viewModel.getAllCategories().observe(this, adapter::setCategories);
        lvCategories.setAdapter(this.adapter);

        ImageButton returnHomeButton = binding.btnReturn;
        returnHomeButton.setOnClickListener(v -> finish());
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category clicked = categories.get(position);
                Intent intent2 = new Intent(CategoriesActivity.this, SubcategoriesActivity.class);
                intent2.putExtra("activeUserName", activeUserName);
                intent2.putExtra("token", userToken);
                intent2.putExtra("categoryId", clicked.getId());
                intent2.putExtra("categoryName", clicked.getName());
                startActivity(intent2);
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        this.viewModel.reload();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not needed in this case
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Get the search query
        String query = s.toString().trim();

        // Normalize the query to avoid different Unicode representations
        query = Normalizer.normalize(query, Form.NFD);
        query = query.replaceAll("[^\\p{ASCII}]", ""); // Remove diacritics if needed (for Hebrew it should not remove anything, but it's good for other languages)

        // Filter the categories based on the normalized query
        List<Category> filteredCategories = new ArrayList<>();

        // Update the categories list with the current database categories
        categories.clear();
        categories.addAll(viewModel.getAllCategories().getValue());

        if (query.isEmpty()) {
            // If the query is empty, show all categories
            filteredCategories.addAll(categories);
        } else {

            for (Category category : categories) {

                // Normalize the category name to avoid Unicode differences
                String categoryName = Normalizer.normalize(category.getName().trim(), Form.NFD);
                categoryName = categoryName.replaceAll("[^\\p{ASCII}]", "");

                Log.d("SearchQuery", "Normalized Query: " + query);
                Log.d("CategoryName", "Normalized Category: " + categoryName);

                // Convert both the category name and query text to lowercase
                if (categoryName.contains(query)) {
                    filteredCategories.add(category);
                }
            }
        }

        // Update the categories list in the adapter
        adapter.setCategories(filteredCategories);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void afterTextChanged(Editable s) {
        // Not needed in this case
    }

}