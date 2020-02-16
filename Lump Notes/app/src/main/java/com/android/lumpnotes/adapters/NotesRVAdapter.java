package com.android.lumpnotes.adapters;

import android.app.Activity;
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
import com.android.lumpnotes.utils.AppUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesRVAdapter extends RecyclerView.Adapter<NotesRVAdapter.NotesRV_VH> {
    private List<Notes> notesList;
    private EmptyNotes emptyNotes;
    private Activity activity;

    public static class NotesRV_VH extends RecyclerView.ViewHolder {
        public ImageView notesIcView;
        public TextView noteTitle;
        public TextView noteTime;
        public TextView noteDate;

        public NotesRV_VH(View v) {
            super(v);
            notesIcView = v.findViewById(R.id.notes_ic_view);
            noteTitle = v.findViewById(R.id.notes_txt);
            noteTime = v.findViewById(R.id.notes_time);
            noteDate = v.findViewById(R.id.notes_date);
        }
    }

    public NotesRVAdapter(List<Notes> notesList,EmptyNotes emptyNotes,Activity activity) {
        this.notesList = notesList;
        this.emptyNotes = emptyNotes;
        this.activity = activity;
    }


    @Override
    public NotesRV_VH onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_rc_items, parent, false);
        NotesRV_VH vh = new NotesRV_VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRV_VH holder, final int position) {
        try {
            Date createdDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(notesList.get(position).getNoteCreatedTimeStamp());
            holder.noteDate.setText(AppUtils.getDatefromTimeStamp(createdDate));
            holder.noteTime.setText(AppUtils.getTimefromTimeStamp(createdDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.noteTitle.setText(notesList.get(position).getNoteTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notesList!=null) {
                    Intent i=new Intent(activity.getApplicationContext(), AddNotesActivity.class);
                    String selectedNote = new Gson().toJson(notesList.get(position));
                    i.putExtra("selectedNote",selectedNote);
                    activity.startActivityForResult(i,1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(notesList!=null && !notesList.isEmpty()) {
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
