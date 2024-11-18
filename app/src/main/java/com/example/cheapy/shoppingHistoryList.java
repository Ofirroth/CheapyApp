package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CartDao;
import com.example.cheapy.adapters.HistoryShopAdapter;
import com.example.cheapy.adapters.ItemCartAdapter;
import com.example.cheapy.adapters.SubCategoryAdapter;
import com.example.cheapy.databinding.ActivityNewCartBinding;
import com.example.cheapy.databinding.ShoppingHistoryBinding;
import com.example.cheapy.databinding.ShoppingHistoryListBinding;
import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.shoppingListHistoryItem;
import com.example.cheapy.viewModels.CartViewModel;
import com.example.cheapy.viewModels.CheckOutViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class shoppingHistoryList extends AppCompatActivity {

    private String userToken;

    private ItemCartAdapter itemCartAdapter;
    private List<Item> cartItems;

    private RecyclerView recyclerView;
    private ShoppingHistoryListBinding binding;
    private CartViewModel cartViewModel;
    String activeUserName;
    String storeName;
    double totalPrice;
    String cartId;
    int quantity = 0;
    private String date;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShoppingHistoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
            cartId = getIntent().getStringExtra("cartId");
            storeName = getIntent().getStringExtra("store_name");
            totalPrice = getIntent().getDoubleExtra("total_price",0.0);
            date = getIntent().getStringExtra("date");
            image = getIntent().getStringExtra("image");
        }
        recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();
        itemCartAdapter = new ItemCartAdapter(cartItems);
        this.cartViewModel = new CartViewModel(this.userToken);
        this.cartViewModel.getSpecificItems(cartId).observe(this, items -> {
            if (items != null) {
                for (Item item:items) {
                    quantity += item.getQuantity();
                    Log.d("msg", String.valueOf(quantity));
                }
                TextView itemCount = binding.itemCountBadge;
                itemCount.setText(String.valueOf(quantity));
                itemCartAdapter.setItems(items); // Update the adapter with the items
            } else {
                Log.d("shoppingHistoryList", "No items found");
            }
        });
        recyclerView.setAdapter(itemCartAdapter);
        TextView totalPriceTextView = binding.totalPriceTextView;
        totalPriceTextView.setText(storeName + ": ₪" + String.format("%.2f", totalPrice));

        ImageButton returnHomeButton = binding.btnReturnHome;
        returnHomeButton.setOnClickListener(v -> finish());

        TextView listTitle = binding.listTitle;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date newdate = inputFormat.parse(date); // Parse the input date string
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateCreated = outputFormat.format(newdate); // Format the parsed Date object
            listTitle.setText(storeName + " - " + dateCreated);
            ImageView imageView = binding.storeImageView;
            Glide.with(Cheapy.context)
                    .load(image)
                    .into(imageView);
        } catch (ParseException e) {
            e.printStackTrace();
            listTitle.setText(storeName + "  Invalid date");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
