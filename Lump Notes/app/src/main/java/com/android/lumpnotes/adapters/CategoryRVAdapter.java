package com.android.lumpnotes.adapters;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;


public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.MyViewHolder>{
    private String[] mDataset;
    private PopupMenu popupMenu;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView categoryIcBorder;
        public ImageView categoryIcView;
        public TextView categoryName;
        public TextView categoryDate;
        public Button menuButton;

        public MyViewHolder(View v) {
            super(v);
            categoryIcBorder = v.findViewById(R.id.category_ic_border);
            categoryIcView = v.findViewById(R.id.category_ic_view);
            categoryName = v.findViewById(R.id.category_name);
            categoryDate = v.findViewById(R.id.category_date);
            menuButton = v.findViewById(R.id.menuBtn);
        }
    }

    public CategoryRVAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public CategoryRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_rc_items, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.MyViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("item clicked"+mDataset[position]);
            }
        });
        holder.categoryName.setText(mDataset[position]);
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu = new PopupMenu(v.getContext(), v);
                createMenu(popupMenu.getMenu(),position);
                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        popupMenu = null;
                    }
                });
                popupMenu.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    private void createMenu(Menu menu,final int position) {
        menu.add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("edit clicked for "+ mDataset[position]);
                return true;
            }
        });

        menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("delete clicked for "+ mDataset[position]);
                return true;
            }
        });
    }
}
