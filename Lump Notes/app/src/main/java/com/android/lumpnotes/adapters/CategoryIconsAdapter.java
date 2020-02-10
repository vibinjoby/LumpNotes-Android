package com.android.lumpnotes.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.lumpnotes.R;

public class CategoryIconsAdapter extends BaseAdapter {
    Resources resourcesObj;
    public int selectedItemIndex = -1;
    ViewHolder holder;

    public CategoryIconsAdapter(Resources resourcesObj) {
        this.resourcesObj = resourcesObj;
    }

    @Override
    public int getCount() {
        return 59;
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

        int imagePos = position + 1;

        String uri = "com.android.lumpnotes:drawable/ctrgy_ic_"+imagePos;

        int res = resourcesObj.getIdentifier(uri, null, null);

        holder.categoryIcon.setBackgroundResource(res);

        if (selectedItemIndex == position) {
            holder.categoryView.setBackgroundResource(R.drawable.selected_btn_view);
        } else {
            holder.categoryView.setBackgroundResource(R.drawable.rounded_ctgry_view);
        }
        convertView.setTag(holder);
        return convertView;
    }

    class ViewHolder {
        TextView categoryIcon;
        TextView categoryView;
    }
}
