package com.example.cheapy.Home_page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Cart.CartManager;
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

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productImage.setImageResource(product.getImageResourceId());

        // Convert the double price to a formatted string
        String formattedPrice = String.format("₪%.2f", product.getPrice());
        holder.productPrice.setText(formattedPrice);

        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        // Update visibility of minus button based on quantity
        if (product.getQuantity() > 0) {
            holder.minusButton.setVisibility(View.VISIBLE);
        } else {
            holder.minusButton.setVisibility(View.GONE);
        }

        // Handle "+" button click
        holder.plusButton.setOnClickListener(v -> {
            int newQuantity = product.getQuantity() + 1;
            product.setQuantity(newQuantity);

            // Update CartManager with the new product or updated quantity
            CartManager.getInstance().addProduct(product);

            // Update the displayed quantity
            holder.productQuantity.setText(String.valueOf(newQuantity));
            holder.minusButton.setVisibility(View.VISIBLE); // Show minus button if quantity > 0
        });

        holder.minusButton.setOnClickListener(v -> {
            int newQuantity = product.getQuantity() - 1;
            if (newQuantity > 0) {
                product.setQuantity(newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
                CartManager.getInstance().addProduct(product); // Update CartManager with the decreased quantity
            } else {
                // If quantity is zero, remove product from the cart
                CartManager.getInstance().removeProduct(product);
                holder.minusButton.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage, plusButton, minusButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productImage = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
        }
    }
}
