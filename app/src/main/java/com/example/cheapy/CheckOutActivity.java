package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.ItemDao;
import com.example.cheapy.Dao.StoreDao;
import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.adapters.ItemAdapter;
import com.example.cheapy.adapters.StoreAdapter;
import com.example.cheapy.databinding.ActivityCheckoutPageBinding;
import com.example.cheapy.databinding.HomePageBinding;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;
import com.example.cheapy.viewModels.CategoryViewModel;
import com.example.cheapy.viewModels.CheckOutViewModel;
import com.example.cheapy.viewModels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckOutActivity extends AppCompatActivity {
    private RecyclerView recyclerViewStores;
    private ActivityCheckoutPageBinding binding;
    private StoreAdapter storeAdapter;
    private Button btnProceedToCheckout;
    private double totalPrice;
    private CheckOutViewModel viewModel;
    private List<Store> listStores;
    private Store selectedStore;
    private AppDB db;
    private StoreDao storeDao;
    String activeUserName;
    String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }

        this.db = DatabaseManager.getDatabase();
        this.storeDao = db.storeDao();
        this.viewModel = new CheckOutViewModel(this.userToken);
        this.listStores = new ArrayList<>();

        GridView lvItems = binding.gridViewStores;
        storeAdapter = new StoreAdapter(CheckOutActivity.this, this.listStores, userToken);

        // Set the selection listener
        storeAdapter.setOnStoreSelectedListener(new StoreAdapter.OnStoreSelectedListener() {
            @Override
            public void onStoreSelected(Store store) {
                selectedStore = store;
            }
        });

        this.viewModel.getStores().observe(this, stores -> {
            if (stores != null && !stores.isEmpty()) {
                storeAdapter.setStores(stores);
                lvItems.setAdapter(storeAdapter);

                listStores.clear();
                listStores.addAll(stores);

                calculateTotalForSelectedStore();
            } else {
                Log.d("CheckOutActivity", "Store list is empty or null.");
            }
        });

        this.btnProceedToCheckout = binding.btnProceedToCheckout;

        ImageButton returnHomeButton = binding.btnReturnHome;
        returnHomeButton.setOnClickListener(v -> finish());

        calculateTotalForSelectedStore();

        btnProceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStore != null) {
                    Intent intent = new Intent(CheckOutActivity.this, NewCartActivity.class);
                    intent.putExtra("store_id", selectedStore.getId());
                    intent.putExtra("store_name", selectedStore.getName());
                    intent.putExtra("user_token", userToken);
                    intent.putExtra("total_price", "0.00");
                    //intent.putExtra("total_price", selectedStore.getTotalPrice());
                    startActivity(intent);
                } else {
                    Toast.makeText(CheckOutActivity.this, "Please select a store to continue.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void calculateTotalForSelectedStore() {
        List<Item> selectedStoreItems = new ArrayList<>(CartManager.getInstance().getCartProducts());
        viewModel.fetchTotalPriceByStore(userToken, listStores, selectedStoreItems);

        for (Store store : listStores) {
            viewModel.getTotalPriceLiveDataForStore(store).observe(this, totalPrice -> {
                if (totalPrice != null) {
                    store.setTotalPrice(totalPrice);
                    Log.d("boo2", String.valueOf(store.getTotalPrice()));
                    storeAdapter.notifyDataSetChanged();  // Notify adapter to refresh the UI
                }
            });
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        calculateTotalForSelectedStore();

    }
}