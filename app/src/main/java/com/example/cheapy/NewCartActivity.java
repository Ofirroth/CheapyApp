package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.adapters.ItemCartAdapter;
import com.example.cheapy.databinding.ActivityCheckoutPageBinding;
import com.example.cheapy.databinding.ActivityNewCartBinding;
import com.example.cheapy.entities.Item;
import com.example.cheapy.viewModels.CartViewModel;
import com.example.cheapy.viewModels.CartViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewCartActivity extends AppCompatActivity {

    private String userToken;

    private ItemCartAdapter itemCartAdapter;
    private List<Item> cartItems;

    private RecyclerView recyclerView;
    private ActivityNewCartBinding binding;

    private CartViewModel cartViewModel;


    String activeUserName;

    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
            storeName = getIntent().getStringExtra("store_name");

        }
        recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        cartItems = new ArrayList<>();
        itemCartAdapter = new ItemCartAdapter(cartItems);
        recyclerView.setAdapter(itemCartAdapter);

        CartViewModelFactory factory = new CartViewModelFactory(userToken);
        cartViewModel = new ViewModelProvider(this, factory).get(CartViewModel.class);

        updateCartProductList();

        updateCartDetails();

        ImageButton returnHomeButton = binding.btnReturnHome;
        returnHomeButton.setOnClickListener(v -> finish());

    }

    private void calculateTotalForSelectedStore() {

        List<Item> selectedStoreItems = CartManager.getInstance().getCartProducts().stream()
                .filter(item -> item.getStore().getName().equals(storeName))
                .collect(Collectors.toList());

        cartViewModel.fetchTotalPriceByStore(userToken, storeName, selectedStoreItems);
        cartViewModel.getTotalPriceLiveData().observe(this, total -> {
            TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
            totalPriceTextView.setText("Total for " + storeName + ": ₪" + String.format("%.2f", total));
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateCartDetails() {

        TextView itemCount = binding.itemCountBadge;
        itemCount.setText(String.valueOf(CartManager.getInstance().getTotalItemCount()));
        calculateTotalForSelectedStore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartProductList();
        updateCartDetails();
    }
    private void updateCartProductList() {
        List<Item> allItems = CartManager.getInstance().getCartProducts();
        cartItems.clear();
        for (Item item : allItems) {
            if (item.getQuantity() > 0) {
                cartItems.add(item);
            }
        }
    }


}
