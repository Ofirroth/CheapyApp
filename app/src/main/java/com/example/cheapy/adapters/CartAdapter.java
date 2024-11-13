package com.example.cheapy.adapters;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Item> cartItems;
    private OnCartItemChangeListener onCartItemChangeListener;


    public CartAdapter(List<Item> cartItems) {
        this.cartItems = cartItems;
    }

    // Define the interface for callbacks
    public interface OnCartItemChangeListener {
        void onCartItemChange();
    }

    public void setOnCartItemChangeListener(OnCartItemChangeListener listener) {
        this.onCartItemChangeListener = listener;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Item item = cartItems.get(position);
        holder.productName.setText(item.getName());
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));

        if (item.getQuantity() > 0) {
            holder.minusButton.setVisibility(View.VISIBLE);
        } else {
            holder.minusButton.setVisibility(View.GONE);
        }
        if (CartManager.getInstance().getProduct(item.getId()) != null){
            int ItemQuantity = CartManager.getInstance().getProduct(item.getId()).getQuantity();
            holder.productQuantity.setText(String.valueOf(ItemQuantity));
            if (ItemQuantity > 0) {
                holder.minusButton.setVisibility(View.VISIBLE);
            } else {
                holder.minusButton.setVisibility(View.GONE);
            }
        }

        holder.plusButton.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(item);
            Item item1 = CartManager.getInstance().getProduct(item.getId());
            holder.productQuantity.setText(String.valueOf(item1.getQuantity()));
            holder.minusButton.setVisibility(View.VISIBLE);
            if (onCartItemChangeListener != null) {
                onCartItemChangeListener.onCartItemChange();
            }
        });

        holder.minusButton.setOnClickListener(v -> {
            CartManager.getInstance().decreaseProductQuantity(item);
            Item item1 = CartManager.getInstance().getProduct(item.getId());
            holder.productQuantity.setText(String.valueOf(item1.getQuantity()));

            if (item.getQuantity() <= 0) {
                item.setQuantity(0);
                holder.productQuantity.setText("0");
                holder.minusButton.setVisibility(View.GONE);
            }
            if (onCartItemChangeListener != null) {
                onCartItemChangeListener.onCartItemChange();
            }
        });
        // Load image using Glide
        Glide.with(Cheapy.context)
                .load(item.getImageResource())
                .into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity;
        ImageView productImage,  plusButton, minusButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
        }
    }
}
