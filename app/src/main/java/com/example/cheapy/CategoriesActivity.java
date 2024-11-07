package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.GridView;

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

import java.util.ArrayList;
import java.util.List;

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
                Intent profileIntent = new Intent(CategoriesActivity.this, HomePageActivity.class);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.navigationHome) {
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(CategoriesActivity.this, CartActivity.class);
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

        // Filter the contacts based on the query
        List<Category> filteredCategories = new ArrayList<>();

        // Update the contacts list with the current database contacts
        categories.clear();
        categories.addAll(viewModel.getAllCategories().getValue());

        if (query.isEmpty()) {
            // If the query is empty, show all contacts
            filteredCategories.addAll(categories);
        } else {
            for (Category category : categories) {
                if (category.getName().toLowerCase().startsWith(query.toLowerCase())) {
                    filteredCategories.add(category);
                }
            }
        }

        // Update the contact list in the adapter
        adapter.setCategories(filteredCategories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Not needed in this case
    }

}