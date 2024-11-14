package com.example.cheapy.adapters;


import static android.text.method.TextKeyListener.clear;
import static java.util.Collections.addAll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Item;
import com.example.cheapy.R;

import java.util.List;

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.ItemCartViewHolder> {

    private List<Item> cartItems;

    public ItemCartAdapter(List<Item> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ItemCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_cart_product, parent, false);
        return new ItemCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCartViewHolder holder, int position) {
        Item item = cartItems.get(position);
        holder.productName.setText(item.getName());
        double itemTotalPrice = (item.getPrice() * item.getQuantity());
        holder.productPrice.setText(String.format("₪ %.2f", itemTotalPrice));
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));

        // Load image using Glide
        Glide.with(Cheapy.context)
                .load(item.getImageResource())
                .into(holder.productImage);


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ItemCartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;

        public ItemCartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
