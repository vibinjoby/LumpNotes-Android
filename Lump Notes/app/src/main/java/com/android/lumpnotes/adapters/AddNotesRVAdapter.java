package com.android.lumpnotes.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddNotesRVAdapter extends RecyclerView.Adapter<AddNotesRVAdapter.AddNotesVH> {
    public class AddNotesVH extends RecyclerView.ViewHolder{
        public AddNotesVH(@NonNull View itemView) {
            super(itemView);
        }
    }
    @NonNull
    @Override
    public AddNotesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AddNotesVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
