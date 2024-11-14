package com.example.cheapy.Cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.CategoriesActivity;
import com.example.cheapy.CheckOutActivity;
import com.example.cheapy.Home_page.HomePageActivity;
import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.entities.Item;
import com.example.cheapy.R;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Item> cartItems;

    String activeUserName;

    String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnCartItemChangeListener(this::updateCartDetails);


        updateCartProductList();

        updateCartDetails();

        ImageButton returnHomeButton = findViewById(R.id.btnReturnHome);
        returnHomeButton.setOnClickListener(v -> finish());

        Button checkoutButton = findViewById(R.id.btnCheckout);
        checkoutButton.setOnClickListener(v ->  {
            Intent nintent = new Intent(CartActivity.this, CheckOutActivity.class);
            nintent.putExtra("activeUserName", activeUserName);
            nintent.putExtra("token", userToken);
            startActivity(nintent);
        });

    }

    @SuppressLint("DefaultLocale")
    private void updateCartDetails() {

        TextView itemCount = findViewById(R.id.itemCountBadge);
        itemCount.setText(String.valueOf(CartManager.getInstance().getTotalItemCount()));
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
