package com.android.lumpnotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;


public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.MyViewHolder>{
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView categoryIcBorder;
        public ImageView categoryIcView;
        public TextView categoryName;
        public TextView categoryDate;
        public MyViewHolder(View v) {
            super(v);
            categoryIcBorder = v.findViewById(R.id.category_ic_border);
            categoryIcView = v.findViewById(R.id.category_ic_view);
            categoryName = v.findViewById(R.id.category_name);
            categoryDate = v.findViewById(R.id.category_date);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryRVAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_rc_items, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.MyViewHolder holder, int position) {
        holder.categoryName.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
