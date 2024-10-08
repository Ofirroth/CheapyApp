package com.example.cheapy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.R;
import com.example.cheapy.entities.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    LayoutInflater inflater;
    private List<Item> itemList;

    public ItemAdapter(Context context, List<Item> itemList) {
        super(context, 0, itemList);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position);
        convertView = inflater.inflate(R.layout.item_product, parent, false);
        TextView name = convertView.findViewById(R.id.productName);
        ImageView imageView = convertView.findViewById(R.id.productImage);
        TextView price = convertView.findViewById(R.id.productPrice);
        TextView quantity = convertView.findViewById(R.id.productQuantity);
        name.setText(item.getName());
        price.setText(String.valueOf(item.getPrice()));
        //quantity.setText(0);


        return convertView;
    }

    public void setItems(List<Item> items) {
        this.clear();
        if (items != null) {
            addAll(items);
        }
        notifyDataSetChanged();
    }

    /**
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.productName.setText(item.getName());
        holder.productImage.setImageResource(Integer.parseInt(item.getImageResource()));

        @SuppressLint("DefaultLocale") String formattedPrice = String.format("₪%.2f", item.getPrice());
        holder.productPrice.setText(formattedPrice);

        // Set the current product quantity
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));

        // Show or hide the minus button based on the quantity
        if (item.getQuantity() > 0) {
            holder.minusButton.setVisibility(View.VISIBLE);
        } else {
            holder.minusButton.setVisibility(View.GONE);
        }

        // Remove any existing click listeners before adding new ones
        holder.plusButton.setOnClickListener(null);
        holder.minusButton.setOnClickListener(null);

        // Set the click listener for the "+" button
        holder.plusButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);

            CartManager.getInstance().addProduct(item);

            holder.productQuantity.setText(String.valueOf(newQuantity));
            holder.minusButton.setVisibility(View.VISIBLE);
        });

        // Set the click listener for the "-" button
        holder.minusButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
                CartManager.getInstance().addProduct(item);
            } else {
                item.setQuantity(0);
                holder.productQuantity.setText("0");
                CartManager.getInstance().removeProduct(item);
                holder.minusButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
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
    }**/
}
