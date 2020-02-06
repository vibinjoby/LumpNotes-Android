package com.android.lumpnotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;

public class NotesRVAdapter extends RecyclerView.Adapter<NotesRVAdapter.MyViewHolder> {
    private String[] mDataset;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView notesIcView;
        public TextView noteTitle;
        public TextView noteTime;
        public TextView noteDate;
        public MyViewHolder(View v) {
            super(v);
            notesIcView = v.findViewById(R.id.notes_ic_view);
            noteTitle = v.findViewById(R.id.notes_txt);
            noteTime = v.findViewById(R.id.notes_time);
            noteDate = v.findViewById(R.id.notes_date);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public NotesRVAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotesRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_rc_items, parent, false);
        NotesRVAdapter.MyViewHolder vh = new NotesRVAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRVAdapter.MyViewHolder holder, int position) {
        holder.noteTitle.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
