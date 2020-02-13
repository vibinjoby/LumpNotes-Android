package com.android.lumpnotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.models.Notes;

import java.util.List;

public class TrashNotesRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Notes> deleteNotes;
    boolean isTestData = true;
    int SMALL_VIEW_TYPE = 1;
    int LARGE_VIEW_TYPE = 2;

    public TrashNotesRVAdapter(List<Notes> deleteNotes) {
        this.deleteNotes = deleteNotes;
    }

    public class SmallNotesViewHolder extends RecyclerView.ViewHolder {
        private TextView notesTitle;
        public SmallNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.small_title_txt);
        }
    }

    public class LargeNotesViewHolder extends RecyclerView.ViewHolder {
        private TextView notesTitle;
        public LargeNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.large_title_txt);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SMALL_VIEW_TYPE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trash_rc_items_small, parent, false);
            TrashNotesRVAdapter.SmallNotesViewHolder vh = new TrashNotesRVAdapter.SmallNotesViewHolder(v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trash_rc_items_large, parent, false);
            TrashNotesRVAdapter.LargeNotesViewHolder vh = new TrashNotesRVAdapter.LargeNotesViewHolder(v);
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0 || (position+1) % 4 == 0) {
            return SMALL_VIEW_TYPE;
        } else if(position % 4 == 0){
            return SMALL_VIEW_TYPE;
        } else {
            return LARGE_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(isTestData) {
            return 19;
        }
        return deleteNotes.size();
    }
}