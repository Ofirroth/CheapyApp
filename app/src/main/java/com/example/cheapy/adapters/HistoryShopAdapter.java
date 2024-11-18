package com.example.cheapy.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.cheapy.API.CartAPI;
import com.example.cheapy.API.StoreAPI;
import com.example.cheapy.R;
import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Store;
import com.example.cheapy.entities.shoppingListHistoryItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryShopAdapter extends ArrayAdapter<shoppingListHistoryItem> {

    private List<shoppingListHistoryItem> carts;
    private LayoutInflater inflater;
    String token;

    public HistoryShopAdapter(Context context, List<shoppingListHistoryItem> carts, String token) {
        super(context, 0, carts);
        this.carts = carts;
        this.inflater = LayoutInflater.from(context);
        this.token = token;
    }

    // Method to update the categories list
    public void setCarts(List<shoppingListHistoryItem> carts) {
        this.clear();
        if (carts != null) {
            addAll(carts);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cart_history_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.storeName = convertView.findViewById(R.id.storeNameTextView);
            viewHolder.date = convertView.findViewById(R.id.dateTextView);
            viewHolder.storeCity = convertView.findViewById(R.id.storeCityTextView);
            viewHolder.storeImage = convertView.findViewById(R.id.storeImageView);
            viewHolder.storeTotalPrice = convertView.findViewById(R.id.storeTotalPriceTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        shoppingListHistoryItem cart = carts.get(position);
        viewHolder.storeName.setText(cart.getStoreName());
        viewHolder.storeCity.setText(cart.getStoreCity());
        if (viewHolder != null && viewHolder.storeTotalPrice != null) {
            viewHolder.storeTotalPrice.setText(String.format("₪ %.2f", cart.getTotalPrice()));
        }
        String imageResource = cart.getStoreImage();
        Glide.with(convertView.getContext())
                .load(imageResource)
                .into(viewHolder.storeImage);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = inputFormat.parse(cart.getDate());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateCreated = outputFormat.format(date);
            viewHolder.date.setText(dateCreated);
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.date.setText(cart.getDate());  // In case of error, fallback to original date
        }
        return convertView;
    }
    private static class ViewHolder {
        TextView storeName, storeCity, storeTotalPrice, date;
        ImageView storeImage;
    }
}
