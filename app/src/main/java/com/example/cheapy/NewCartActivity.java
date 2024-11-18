package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.adapters.ItemCartAdapter;
import com.example.cheapy.databinding.ActivityCheckoutPageBinding;
import com.example.cheapy.databinding.ActivityNewCartBinding;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;
import com.example.cheapy.viewModels.CartViewModel;
import com.example.cheapy.viewModels.CheckOutViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewCartActivity extends AppCompatActivity {

    private String userToken;

    private ItemCartAdapter itemCartAdapter;
    private List<Item> cartItems;

    private RecyclerView recyclerView;
    private ActivityNewCartBinding binding;

    private CheckOutViewModel viewModel;
    private CartViewModel cartViewModel;


    String activeUserName;

    String storeName;
    double totalPrice;
    String storeId;

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
            storeId = getIntent().getStringExtra("store_id");
            totalPrice = getIntent().getDoubleExtra("total_price",0.0);
        }
        recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        cartItems = new ArrayList<>();
        itemCartAdapter = new ItemCartAdapter(cartItems);
        recyclerView.setAdapter(itemCartAdapter);
        this.cartViewModel = new CartViewModel(this.userToken);
        this.viewModel = new CheckOutViewModel(this.userToken);
        updateCartProductList();
        updateCartDetails();
        ImageButton returnHomeButton = binding.btnReturnHome;
        returnHomeButton.setOnClickListener(v -> finish());

        Button finishButton = binding.btnCheckout;
        finishButton.setOnClickListener(v -> {
            saveCart();
        });
    }

    private void saveCart() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateCreated = sdf.format(new Date());
        cartViewModel.createCart(userToken, activeUserName, storeId, cartItems, totalPrice, dateCreated);
        Intent finishIntent = new Intent(NewCartActivity.this, FinishActivity.class);
        finishIntent.putExtra("activeUserName", activeUserName);
        finishIntent.putExtra("token", userToken);
        startActivity(finishIntent);
    }

    @SuppressLint("DefaultLocale")
    private void updateCartDetails() {
        TextView itemCount = binding.itemCountBadge;
        itemCount.setText(String.valueOf(CartManager.getInstance().getTotalItemCount()));
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        totalPriceTextView.setText(storeName + ": ₪" + String.format("%.2f", totalPrice));
    }
    private void calculatePriceForSelectedItems() {
        List<Item> selectedStoreItems = new ArrayList<>(CartManager.getInstance().getCartProducts());
        for (Item item : selectedStoreItems) {
            viewModel.fetchItemPriceByStore(userToken, storeId, item.getId());
            viewModel.getItemPriceLiveDataForStore(item).observe(this, price -> {
                if (price != null) {
                    item.setPrice(price);
                    itemCartAdapter.notifyDataSetChanged();
                }
            });
        }
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
