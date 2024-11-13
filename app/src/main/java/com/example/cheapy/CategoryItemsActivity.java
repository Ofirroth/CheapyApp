package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.Dao.ItemDao;
import com.example.cheapy.Home_page.HomePageActivity;
import com.example.cheapy.adapters.CategoryAdapter;
import com.example.cheapy.adapters.ItemAdapter;
import com.example.cheapy.adapters.itemListAdapter;
import com.example.cheapy.databinding.ActivityCategoriesBinding;
import com.example.cheapy.databinding.CategoryItemsBinding;
import com.example.cheapy.databinding.HomePageBinding;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.viewModels.CategoryViewModel;
import com.example.cheapy.viewModels.ItemViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemsActivity extends AppCompatActivity implements TextWatcher {
    String activeUserName;
    private CategoryItemsBinding binding;

    String userToken;

    private Boolean isNightMode = null;
    private AppDB db;
    private ItemDao itemDao;
    private itemListAdapter itemAdapter;
    private ItemViewModel itemViewModel;
    private List<Item> items;
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CategoryItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
            categoryId = getIntent().getIntExtra("subCategoryId", -1);
            categoryName = getIntent().getStringExtra("subCategoryName");
        }
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationMyProfile) {
                Intent profileIntent = new Intent(CategoryItemsActivity.this, profilePageActivity.class);
                profileIntent.putExtra("activeUserName", activeUserName);
                profileIntent.putExtra("token", userToken);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.navigationHome) {
                finish();
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(CategoryItemsActivity.this, CartActivity.class);
                cartIntent.putExtra("activeUserName", activeUserName);
                cartIntent.putExtra("token", userToken);
                startActivity(cartIntent);
                return true;
            } else {
                return false;
            }
        });

        this.db = DatabaseManager.getDatabase();
        this.itemDao = db.itemDao();
        this.itemViewModel = new ItemViewModel(this.userToken);
        this.items = new ArrayList<>();

        binding.searchEditText.addTextChangedListener(this);
        binding.searchEditText.addTextChangedListener(this);

        GridView lvCategoriesItems = binding.gridViewCategoriesItems;
        this.itemAdapter = new itemListAdapter(getApplicationContext(), this.items);
        this.itemViewModel.getItemsByCategory(categoryId).observe(this, itemAdapter::setItems);
        lvCategoriesItems.setAdapter(this.itemAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        this.itemViewModel.getItemsByCategory(categoryId);
    }

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
        List<Item> filteredItems = new ArrayList<>();

        // Update the contacts list with the current database contacts
        items.clear();
        items.addAll(this.itemViewModel.getItemsByCategory(categoryId).getValue());

        if (query.isEmpty()) {
            // If the query is empty, show all contacts
            filteredItems.addAll(items);
        } else {
            for (Item item : items) {
                if (item.getName().toLowerCase().startsWith(query.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }

        // Update the contact list in the adapter
        itemAdapter.setItems(filteredItems);
        itemAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Not needed in this case
    }

}
