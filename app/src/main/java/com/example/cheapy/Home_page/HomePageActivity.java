package com.example.cheapy.Home_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.CategoriesActivity;
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
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }

        // Setup the toolbar click listener to navigate to CategoriesActivity
        ImageView toolbar = binding.menuButton;  // Get the toolbar
        toolbar.setOnClickListener(v -> {
            Intent nintent = new Intent(HomePageActivity.this, CategoriesActivity.class);
            nintent.putExtra("activeUserName", activeUserName);
            nintent.putExtra("token", userToken);
            startActivity(nintent);
        });

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
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
        this.viewModel = new ItemViewModel(this.userToken);
        this.items = new ArrayList<>();

        //initializeProductList();
        GridView lvItems = binding.gridViewProducts;
        adapter = new ItemAdapter(getApplicationContext(), this.items);
        this.viewModel.getItems().observe(this, adapter::setItems);;
        lvItems.setAdapter(adapter);

        //adapter.setNightMode(isNightMode);
        //viewModel.getItems().observe(this, adapter::setItems);
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
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        this.viewModel.reload();
    }
}

