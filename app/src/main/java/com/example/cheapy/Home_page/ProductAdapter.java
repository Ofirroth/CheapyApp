package com.example.cheapy.Home_page;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productImage.setImageResource(product.getImageResourceId());
        holder.productPrice.setText(product.getPrice());

        // Set up hover/touch listener to show the add to cart button
        holder.itemView.setOnHoverListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    holder.addToCartButton.setVisibility(View.GONE);
                    break;
            }
            return true;
        });

        // Alternatively, for touch interaction
        holder.itemView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                holder.addToCartButton.setVisibility(View.VISIBLE);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                holder.addToCartButton.setVisibility(View.GONE);
            }
            return true;
        });

        // Handle click on the Add to Cart button
        holder.addToCartButton.setOnClickListener(v -> {
            // Handle add to cart logic
            Toast.makeText(v.getContext(), product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage, addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productImage = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
