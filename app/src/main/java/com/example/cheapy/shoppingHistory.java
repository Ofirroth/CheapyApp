package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CartDao;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.Home_page.HomePageActivity;
import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.adapters.CategoryAdapter;
import com.example.cheapy.adapters.HistoryShopAdapter;
import com.example.cheapy.databinding.ActivityCategoriesBinding;
import com.example.cheapy.databinding.ShoppingHistoryBinding;
import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.shoppingListHistoryItem;
import com.example.cheapy.viewModels.CartViewModel;
import com.example.cheapy.viewModels.CategoryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class shoppingHistory extends AppCompatActivity {

    String activeUserName;
    private ShoppingHistoryBinding binding;

    String userToken;
    private AppDB db;
    private CartDao cartDao;
    private HistoryShopAdapter adapter;

    private CartViewModel viewModel;
    private List<shoppingListHistoryItem> carts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShoppingHistoryBinding.inflate(getLayoutInflater());
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
                Intent profileIntent = new Intent(shoppingHistory.this, profilePageActivity.class);
                profileIntent.putExtra("activeUserName", activeUserName);
                profileIntent.putExtra("token", userToken);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.navigationHome) {
                Intent homeIntent = new Intent(shoppingHistory.this, HomePageActivity.class);
                homeIntent.putExtra("activeUserName", activeUserName);
                homeIntent.putExtra("token", userToken);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(shoppingHistory.this, CartActivity.class);
                cartIntent.putExtra("activeUserName", activeUserName);
                cartIntent.putExtra("token", userToken);
                startActivity(cartIntent);
                return true;
            } else {
                return false;
            }
        });

        this.db = DatabaseManager.getDatabase();
        this.cartDao = db.cartDao();
        this.viewModel = new CartViewModel(this.userToken);
        this.carts = new ArrayList<>();
        GridView lvCarts = binding.gridViewShoppingHistory;
        this.adapter = new HistoryShopAdapter(Cheapy.context, this.carts, userToken);
        this.viewModel.getUserCart(activeUserName).observe(this, adapter::setCarts);
        lvCarts.setAdapter(this.adapter);

        ImageButton returnHomeButton = binding.btnReturn2;
        returnHomeButton.setOnClickListener(v -> finish());

        lvCarts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shoppingListHistoryItem clicked = carts.get(position);
                Intent intent2 = new Intent(shoppingHistory.this, shoppingHistoryList.class);
                intent2.putExtra("activeUserName", activeUserName);
                intent2.putExtra("token", userToken);
                intent2.putExtra("cartId", clicked.getId());
                intent2.putExtra("store_name", clicked.getStoreName());
                intent2.putExtra("total_price", clicked.getTotalPrice());
                intent2.putExtra("date", clicked.getDate());
                intent2.putExtra("image",clicked.getStoreImage());
                startActivity(intent2);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        this.viewModel.getUserCart(activeUserName);
    }

}
