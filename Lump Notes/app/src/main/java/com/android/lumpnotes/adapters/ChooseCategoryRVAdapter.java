package com.android.lumpnotes.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.listeners.RecyclerViewClickListener;
import com.android.lumpnotes.models.Category;

import java.util.List;

public class ChooseCategoryRVAdapter extends RecyclerView.Adapter<ChooseCategoryRVAdapter.ChooseCategoryVH>  {
    private List<Category> categoryList;
    private Resources resourcesObj;
    private RecyclerViewClickListener itemListener;
    public int ignoreCategoryPos = -1;
    public ChooseCategoryRVAdapter(List<Category> categoryList,Resources resourcesObj, RecyclerViewClickListener itemListener) {
        this.categoryList = categoryList;
        this.resourcesObj = resourcesObj;
        this.itemListener = itemListener;
    }

    public class ChooseCategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView categoryTitle;
        private ImageView categoryIcon;
        private TextView numberOfNotes;

        public ChooseCategoryVH(@NonNull View v) {
            super(v);
            categoryTitle = v.findViewById(R.id.category_name);
            categoryIcon = v.findViewById(R.id.choose_category_ic);
            numberOfNotes = v.findViewById(R.id.num_notes_txt);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }
    @NonNull
    @Override
    public ChooseCategoryRVAdapter.ChooseCategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_category_rc_items, parent, false);
        ChooseCategoryRVAdapter.ChooseCategoryVH vh = new ChooseCategoryRVAdapter.ChooseCategoryVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseCategoryRVAdapter.ChooseCategoryVH holder, int position) {
        if(ignoreCategoryPos != position) {
            holder.categoryTitle.setVisibility(View.VISIBLE);
            holder.categoryIcon.setVisibility(View.VISIBLE);
            holder.numberOfNotes.setVisibility(View.VISIBLE);
            holder.categoryTitle.setText(categoryList.get(position).getCategoryName());
            String uri = "com.android.lumpnotes:drawable/" + categoryList.get(position).getCategoryIcon();
            int res = resourcesObj.getIdentifier(uri, null, null);
            holder.categoryIcon.setImageResource(res);
            if (categoryList.get(position).getNotesList() != null) {
                int numOfNotes = categoryList.get(position).getNotesList().size();
                holder.numberOfNotes.setText(numOfNotes + " Notes");
            } else {
                holder.numberOfNotes.setText(0 + " Notes");
            }
        } else {
            holder.categoryTitle.setVisibility(View.GONE);
            holder.categoryIcon.setVisibility(View.GONE);
            holder.numberOfNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return  categoryList.size();
    }
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }
}
