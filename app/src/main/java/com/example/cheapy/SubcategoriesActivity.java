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
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.Home_page.HomePageActivity;
import com.example.cheapy.adapters.CategoryAdapter;
import com.example.cheapy.adapters.SubCategoryAdapter;
import com.example.cheapy.databinding.SubCategoriesBinding;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.SubCategory;
import com.example.cheapy.viewModels.CategoryViewModel;
import com.example.cheapy.viewModels.SubCategoryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SubcategoriesActivity extends AppCompatActivity implements TextWatcher {
    String activeUserName;
    private SubCategoriesBinding binding;

    String userToken;

    private AppDB db;
    private CategoryDao categoryDao;
    private SubCategoryViewModel subCategoryViewModel;
    private List<SubCategory> subCategories;
    private int categoryId;
    private String categoryName;

    private SubCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SubCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
            categoryId = getIntent().getIntExtra("categoryId", -1);
            categoryName = getIntent().getStringExtra("categoryName");
        }
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationMyProfile) {
                Intent profileIntent = new Intent(SubcategoriesActivity.this, profilePageActivity.class);
                profileIntent.putExtra("activeUserName", activeUserName);
                profileIntent.putExtra("token", userToken);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.navigationHome) {
                finish();
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(SubcategoriesActivity.this, CartActivity.class);
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
        this.subCategoryViewModel = new SubCategoryViewModel(this.userToken, categoryId);
        this.subCategories = new ArrayList<>();
        setTitle(categoryName);
        binding.searchEditText.addTextChangedListener(this);
        binding.searchEditText.addTextChangedListener(this);
        GridView lvCategories = binding.gridViewSubCategories;
        this.adapter = new SubCategoryAdapter(getApplicationContext(), this.subCategories);
        this.subCategoryViewModel.getSubcategoriesByCategoryId().observe(this, adapter::setSubCategories);
        lvCategories.setAdapter(this.adapter);

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategory clicked = subCategories.get(position);
                Intent intent2 = new Intent(SubcategoriesActivity.this, CategoryItemsActivity.class);
                intent2.putExtra("activeUserName", activeUserName);
                intent2.putExtra("token", userToken);
                intent2.putExtra("subCategoryId", clicked.getId());
                intent2.putExtra("subCategoryName", clicked.getName());
                startActivity(intent2);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        this.subCategoryViewModel.reload();
    }
    // Save a flag when finishing
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not needed in this case
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /*// Get the search query
        String query = s.toString().trim();

        // Filter the contacts based on the query
        List<String> filteredCategories = new ArrayList<>();

        // Update the contacts list with the current database contacts
        categories.clear();
        categories.addAll(viewModel.getAllCategories().getValue());

        if (query.isEmpty()) {
            // If the query is empty, show all contacts
            filteredCategories.addAll(sub);
        } else {
            for (String category : sub) {
                if (category.toLowerCase().startsWith(query.toLowerCase())) {
                    filteredCategories.add(category);
                }
            }
        }

        // Update the contact list in the adapter
        adapter.setCategories(filteredCategories);
        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Not needed in this case
    }

}
