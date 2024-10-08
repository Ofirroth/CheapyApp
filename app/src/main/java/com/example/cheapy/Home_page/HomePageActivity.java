package com.example.cheapy.Home_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.ItemDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.adapters.ItemAdapter;
import com.example.cheapy.databinding.HomePageBinding;
import com.example.cheapy.entities.Item;
import com.example.cheapy.R;
import com.example.cheapy.entities.Store;
import com.example.cheapy.viewModels.ItemViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    String activeUserName;
    private HomePageBinding binding;

    String userToken;

    private Boolean isNightMode = null;
    private AppDB db;
    private ItemDao itemDao;
    private ItemAdapter adapter;

    private ItemViewModel viewModel;
    private List<Item> items; // Declare productList as a member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView
        //RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize the product list
        initializeProductList(); // Call a method to initialize the list

        // Create and set the adapter
        //adapter = new ItemAdapter(items);
        //recyclerView.setAdapter(adapter);

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

        this.db = DatabaseManager.getDatabase();

        this.itemDao = db.itemDao();
        this.viewModel = new ItemViewModel(userToken);
        this.items = new ArrayList<>();

        items.add(new Item("טחינה אחווה", String.valueOf(R.drawable.product_1), 4.80, new Store("rami","holon"), "1",0));
        items.add(new Item("טל העמק 9%", String.valueOf(R.drawable.product_2), 16.80, new Store("rami","holon"), "1",0));

        ListView lvItems = binding.listViewProducts;
        adapter = new ItemAdapter(getApplicationContext(), (ArrayList<Item>) this.items);
        //adapter.setNightMode(isNightMode);

        //viewModel.getItems().observe(this, adapter::setItems);

        lvItems.setAdapter(adapter);
        //lvItems.setClickable(true);

        //binding.searchEditText.addTextChangedListener(this);

        //MutableLiveData<String> contactFirebase = SingeltonFireBase.getContactFirebase();
        //MutableLiveData<Message> messageMutableLiveData = SingeltonFireBase.getMessageFirebase();

        /**messageMutableLiveData.observe(this,contacts -> {
            if(contacts!=null) {
                viewModel.getContacts();
            }
        });
        contactFirebase.observe(this,contact -> {
            if (contact != null) {
                //viewModel.addContact(contact);
                viewModel.getContacts();
            }
        });**/
    }

    private void initializeProductList() {
        items = CartManager.getInstance().getCartProducts();
        if (items.isEmpty()) {
            // Populate with initial products if cart is empty
            items.add(new Item("טחינה אחווה", String.valueOf(R.drawable.product_1), 4.80, new Store("rami","holon"), "1",0));
            items.add(new Item("טל העמק 9%", String.valueOf(R.drawable.product_2), 16.80, new Store("rami","holon"), "1",0));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        // Update the product list and cart state when returning to the HomePage
        items = CartManager.getInstance().getCartProducts();
        adapter.notifyDataSetChanged(); // Refresh RecyclerView
    }
}

