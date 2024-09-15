package com.example.cheapy.Home_page;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Home_page.ProductAdapter;
import com.example.cheapy.Home_page.Product;
import com.example.cheapy.R;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create a list of productss
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Product 1", R.drawable.ic_category_fruit, "₪4.80"));
        productList.add(new Product("Product 2", R.drawable.ic_category_fruit, "₪16.80"));
        // Add more products as needed

        // Create and set the adapter
        ProductAdapter adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
    }
}

