package com.example.cheapy.Cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.entities.Item;
import com.example.cheapy.R;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Item> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get cart products from a shared cart manager or singleton class
        updateCartProductList();

        // Update total price and quantity
        updateCartDetails();

        ImageButton returnHomeButton = findViewById(R.id.btnReturnHome);
        returnHomeButton.setOnClickListener(v -> finish());

    }

    private void updateCartProductList() {
        // Get the list of all products from CartManager
        List<Item> allItems = CartManager.getInstance().getCartProducts();

        // Filter products with quantity > 0
        cartItems = new ArrayList<>();
//        for (Item item : allItems) {
//            if (item.getQuantity() > 0) {
//                cartItems.add(item);
//            }
//        }

        // Create and set the adapter with the filtered product list
        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(cartAdapter);
    }

    @SuppressLint("DefaultLocale")
    private void updateCartDetails() {
        TextView totalPrice = findViewById(R.id.totalPrice);
        totalPrice.setText(String.format("₪ %.2f", CartManager.getInstance().getTotalPrice()));

        TextView itemCount = findViewById(R.id.itemCountTextView);
        itemCount.setText(String.valueOf(CartManager.getInstance().getTotalItemCount()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the cart product list when returning to the CartActivity
        updateCartProductList();
        updateCartDetails();
    }
}
