package com.example.cheapy.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.cheapy.R;
import com.example.cheapy.entities.SubCategory;
import java.util.List;


public class SubCategoryAdapter extends ArrayAdapter<SubCategory> {
    private LayoutInflater inflater;
    private List<SubCategory> subcategories;

    public SubCategoryAdapter(Context context, List<SubCategory> subcategories) {
        super(context, 0, subcategories);
        this.subcategories = subcategories;
        this.inflater = LayoutInflater.from(context);
    }

    // Method to update the categories list
    public void setSubCategories(List<SubCategory> subCategories) {
        this.clear();
        if (subCategories != null) {
            addAll(subCategories);
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
        SubCategory subCategory = subcategories.get(position);

        // Set category name
        viewHolder.categoryName.setText(subCategory.getName());

        // Set category image using Glide
        String imageResource = subCategory.getImage();
        Glide.with(convertView.getContext())
                .load(imageResource)
                .into(viewHolder.categoryImage);

        return convertView;
    }

    // ViewHolder class to optimize the performance of the ListView
    private static class ViewHolder {
        TextView categoryName;
        ImageView categoryImage;
    }
}
