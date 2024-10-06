package com.example.cheapy.Cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cheapy.Home_page.Product;
import com.example.cheapy.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartProducts;

    public CartAdapter(List<Product> cartProducts) {
        this.cartProducts = cartProducts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartProducts.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("₪ %.2f", product.getPrice()));
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        // Handle click events or any other functionality
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
