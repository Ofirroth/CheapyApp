package com.example.cheapy.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.R;
import com.example.cheapy.entities.Item;

import java.util.List;

public class itemListAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> itemList;

    public itemListAdapter(Context context, List<Item> itemList) {
        super(context, 0, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.productName = convertView.findViewById(R.id.productName);
            viewHolder.productImage = convertView.findViewById(R.id.productImage);
            viewHolder.productQuantity = convertView.findViewById(R.id.productQuantity);
            viewHolder.plusButton = convertView.findViewById(R.id.plusButton);
            viewHolder.minusButton = convertView.findViewById(R.id.minusButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);

        if (item != null) {
            viewHolder.productName.setText(item.getName());
            viewHolder.productQuantity.setText(String.valueOf(item.getQuantity()));
            if (item.getQuantity() > 0) {
                viewHolder.minusButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.minusButton.setVisibility(View.GONE);
            }
            if (CartManager.getInstance().getProduct(item.getId()) != null){
                int ItemQuantity = CartManager.getInstance().getProduct(item.getId()).getQuantity();
                viewHolder.productQuantity.setText(String.valueOf(ItemQuantity));
                if (ItemQuantity > 0) {
                    viewHolder.minusButton.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.minusButton.setVisibility(View.GONE);
                }
            }
            //Log.d("boo", item.getImageResource());
            /**Glide.with(context)
             .load(item.getImageResource())
             .into(viewHolder.productImage);
             */
            Glide.with(context)
                    .load(item.getImageResource())
                    .into(viewHolder.productImage);



            viewHolder.plusButton.setOnClickListener(v -> {
                CartManager.getInstance().addProduct(item);
                viewHolder.productQuantity.setText(String.valueOf(CartManager.getInstance().getProduct(item.getId()).getQuantity()));
                viewHolder.minusButton.setVisibility(View.VISIBLE);
            });

            viewHolder.minusButton.setOnClickListener(v -> {
                CartManager.getInstance().decreaseProductQuantity(item);
                viewHolder.productQuantity.setText(String.valueOf(CartManager.getInstance().getProduct(item.getId()).getQuantity()));

                if (item.getQuantity() <= 0) {
                    item.setQuantity(0);
                    viewHolder.productQuantity.setText("0");
                    viewHolder.minusButton.setVisibility(View.GONE);
                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView productName, productQuantity;
        ImageView productImage, plusButton, minusButton;
    }

    public void setItems(List<Item> items) {
        clear();
        if (items != null) {
            addAll(items);
        }
        notifyDataSetChanged();
    }
}
