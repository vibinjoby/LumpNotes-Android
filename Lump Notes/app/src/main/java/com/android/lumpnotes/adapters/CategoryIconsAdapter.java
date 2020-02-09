package com.android.lumpnotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.android.lumpnotes.R;

public class CategoryIconsAdapter extends BaseAdapter {
    int selectedItemIndex = -1;
    ViewHolder holder;
    @Override
    public int getCount() {
        return 130;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_icon_view, parent, false);
        holder = new ViewHolder();
        holder.categoryIcon = convertView.findViewById(R.id.icon_btn);
        holder.categoryView = convertView.findViewById(R.id.icon_view);
        holder.categoryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemIndex = position;
                notifyDataSetChanged();
            }
        });

        if(selectedItemIndex == position) {
            holder.categoryView.setBackgroundResource(R.drawable.selected_btn_view);
        } else {
            holder.categoryView.setBackgroundResource(R.drawable.rounded_ctgry_view);
        }

        holder.categoryIcon.setBackgroundResource(R.drawable.music);
        convertView.setTag(holder);
        return convertView;
    }
    class ViewHolder {
        Button categoryIcon;
        View categoryView;
    }
}
