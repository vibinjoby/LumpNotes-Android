package com.android.lumpnotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.utils.AppUtils;

import java.util.List;

public class PinnedNotesRVAdapter extends RecyclerView.Adapter<PinnedNotesRVAdapter.NotesViewHolder> {
    private List<Notes> pinnedNotes;
    boolean isTestData = true;

    public PinnedNotesRVAdapter(List<Notes> pinnedNotes) {
        this.pinnedNotes = pinnedNotes;
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView notesTitle;
        private TextView locationTxt;
        private TextView notesEditedTime;
        private TextView notesEditedDate;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.pinned_notes_title);
            locationTxt = itemView.findViewById(R.id.address_txt);
            notesEditedTime = itemView.findViewById(R.id.pinned_notes_time);
            notesEditedDate = itemView.findViewById(R.id.pinned_notes_date);
        }
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pinned_rc_items, parent, false);
        PinnedNotesRVAdapter.NotesViewHolder vh = new PinnedNotesRVAdapter.NotesViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        if(!isTestData) {
            holder.locationTxt.setText(pinnedNotes.get(position).getAddress());
            holder.notesTitle.setText(pinnedNotes.get(position).getNoteTitle());
            holder.notesEditedDate.setText(AppUtils.getDatefromTimeStamp(pinnedNotes.get(position).getLastEditedTimeStamp()));
            holder.notesEditedTime.setText(AppUtils.getTimefromTimeStamp(pinnedNotes.get(position).getLastEditedTimeStamp()));
        }
    }

    @Override
    public int getItemCount() {
        if(isTestData) {
            return 3;
        }
        return pinnedNotes.size();
    }
}
