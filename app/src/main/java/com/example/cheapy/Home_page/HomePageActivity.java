package com.example.cheapy.Home_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.adapters.ProductAdapter;
import com.example.cheapy.entities.Product;
import com.example.cheapy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private List<Product> productList; // Declare productList as a member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize the product list
        initializeProductList(); // Call a method to initialize the list

        // Create and set the adapter
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        // Set up the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationMyProfile) {
                Intent profileIntent = new Intent(HomePageActivity.this, HomePageActivity.class);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.navigationHome) {
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(HomePageActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else {
                return false;
            }
        });
    }

    private void initializeProductList() {
        productList = CartManager.getInstance().getCartProducts();
        if (productList.isEmpty()) {
            // Populate with initial products if cart is empty
            productList.add(new Product("טחינה אחווה", R.drawable.product_1, 4.80));
            productList.add(new Product("טל העמק 9%", R.drawable.product_2, 16.80));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        // Update the product list and cart state when returning to the HomePage
        productList = CartManager.getInstance().getCartProducts();
        adapter.notifyDataSetChanged(); // Refresh RecyclerView
    }
}

