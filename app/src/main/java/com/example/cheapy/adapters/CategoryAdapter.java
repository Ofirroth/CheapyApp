package com.example.cheapy.adapters;

import static com.example.cheapy.Cheapy.context;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cheapy.R;
import com.example.cheapy.entities.Category;

import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private List<Category> categories;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }

    // Method to update the categories list
    public void setCategories(List<Category> categories) {
        this.clear();
        if (categories != null) {
            addAll(categories);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // Check if the convertView is null, if so, inflate a new view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.categoryName = convertView.findViewById(R.id.categoryNameTextView);
            viewHolder.categoryImage = convertView.findViewById(R.id.categoryImageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the category object for the current position
        Category category = categories.get(position);

        // Set category name
        viewHolder.categoryName.setText(category.getName());

        // Set category image using Glide
        String imageResource = category.getImage();
        Glide.with(convertView.getContext())
                .load(imageResource)
                .into(viewHolder.categoryImage);

        return convertView;
    }

    private static class ViewHolder {
        TextView categoryName;
        ImageView categoryImage;
    }
}
