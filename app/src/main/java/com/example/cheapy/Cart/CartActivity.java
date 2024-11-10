package com.example.cheapy.Cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.adapters.CartAdapter;
import com.example.cheapy.entities.Product;
import com.example.cheapy.R;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateCartProductList();

        updateCartDetails();

        ImageButton returnHomeButton = findViewById(R.id.btnReturnHome);
        returnHomeButton.setOnClickListener(v -> finish());

    }

    private void updateCartProductList() {
        List<Product> allProducts = CartManager.getInstance().getCartProducts();

        cartProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getQuantity() > 0) {
                cartProducts.add(product);
            }
        }

        cartAdapter = new CartAdapter(cartProducts);
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
        updateCartProductList();
        updateCartDetails();
    }
}
