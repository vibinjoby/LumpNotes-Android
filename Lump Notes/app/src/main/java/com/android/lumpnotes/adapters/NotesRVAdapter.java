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
    private String[] notesArr;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
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

    public NotesRVAdapter(String[] notesArr) {
        this.notesArr = notesArr;
    }


    @Override
    public NotesRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_rc_items, parent, false);
        NotesRVAdapter.MyViewHolder vh = new NotesRVAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRVAdapter.MyViewHolder holder, final int position) {
        holder.noteTitle.setText(notesArr[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("item clicked" + notesArr[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesArr.length;
    }
}
