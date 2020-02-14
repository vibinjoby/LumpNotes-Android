package com.android.lumpnotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.activity.AddNotesActivity;
import com.android.lumpnotes.models.EmptyNotes;
import com.android.lumpnotes.models.Notes;

import java.util.List;

public class NotesRVAdapter extends RecyclerView.Adapter<NotesRVAdapter.MyViewHolder> {
    private List<Notes> notesList;
    private EmptyNotes emptyNotes;
    private Context context;

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

    public NotesRVAdapter(List<Notes> notesList,EmptyNotes emptyNotes,Context context) {
        this.notesList = notesList;
        this.emptyNotes = emptyNotes;
        this.context = context;
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
        holder.noteTitle.setText(notesList.get(position).getNoteTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notesList!=null) {
                    Intent i=new Intent(context, AddNotesActivity.class);
                    i.putExtra("notesTitle", notesList.get(position).getNoteTitle());
                    i.putExtra("notesDescription", notesList.get(position).getNoteDescription());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(notesList!=null) {
            emptyNotes.getEmptyNotesView().setVisibility(View.GONE);
            emptyNotes.getSearchIcon().setVisibility(View.VISIBLE);
            emptyNotes.getSearchTxt().setVisibility(View.VISIBLE);
            return notesList.size();
        } else {
            emptyNotes.getEmptyNotesView().setVisibility(View.VISIBLE);
            emptyNotes.getSearchIcon().setVisibility(View.GONE);
            emptyNotes.getSearchTxt().setVisibility(View.GONE);
            return 0;
        }
    }

    public void setNotesList(List<Notes> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }
}
