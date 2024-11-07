package com.example.cheapy.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cheapy.R;

import java.util.List;

public class SubCategoryAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private List<String> subcategories;

    public SubCategoryAdapter(Context context, List<String> subcategories) {
        super(context, 0, subcategories);
        this.subcategories = subcategories;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_subcategory, parent, false);
        }

        TextView subcategoryText = convertView.findViewById(R.id.subCategories2);
        subcategoryText.setText(subcategories.get(position));

        return convertView;
    }
}
