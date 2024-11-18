package com.example.cheapy.Home_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.example.cheapy.SubcategoriesActivity;
import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.adapters.ItemAdapter;
import com.example.cheapy.databinding.HomePageBinding;
import com.example.cheapy.entities.Item;
import com.example.cheapy.R;
import com.example.cheapy.entities.Store;
import com.example.cheapy.profilePageActivity;
import com.example.cheapy.shoppingHistory;
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

    private CartAdapter cartAdapter;
    private CartManager cartManager;

    private BottomNavigationView bottomNavigationView;

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

        ImageView toolbar = binding.menuButton;
        toolbar.setOnClickListener(v -> {
            Intent nintent = new Intent(HomePageActivity.this, CategoriesActivity.class);
            nintent.putExtra("activeUserName", activeUserName);
            nintent.putExtra("token", userToken);
            startActivity(nintent);
        });

        Button history = binding.btnShoppingHistory;
        history.setOnClickListener(v -> {
            Intent nintent = new Intent(HomePageActivity.this, shoppingHistory.class);
            nintent.putExtra("activeUserName", activeUserName);
            nintent.putExtra("token", userToken);
            startActivity(nintent);
        });

        bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationMyProfile) {
                Intent profileIntent = new Intent(HomePageActivity.this, profilePageActivity.class);
                profileIntent.putExtra("activeUserName", activeUserName);
                profileIntent.putExtra("token", userToken);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.navigationHome) {
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(HomePageActivity.this, CartActivity.class);
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
        this.viewModel = new ItemViewModel(this.userToken);
        this.items = new ArrayList<>();

        GridView lvItems = binding.gridViewProducts;
        adapter = new ItemAdapter(getApplicationContext(), this.items);
        this.viewModel.getItems().observe(this, adapter::setItems);;
        lvItems.setAdapter(adapter);

        CartManager.getInstance().getCartLiveData().observe(this, updatedItems -> {
            for (Item updatedItem : updatedItems) {
                for (Item homePageItem : items) {
                    if (homePageItem.getName().equals(updatedItem.getName())) {
                        homePageItem.setQuantity(updatedItem.getQuantity());
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();  // Refresh the adapter
        });

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
            bottomNavigationView.setSelectedItemId(R.id.navigationHome);

    }
}

