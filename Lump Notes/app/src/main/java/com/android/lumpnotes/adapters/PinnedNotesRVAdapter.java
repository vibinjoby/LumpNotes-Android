package com.android.lumpnotes.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.activity.AddNotesActivity;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.utils.AppUtils;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PinnedNotesRVAdapter extends RecyclerView.Adapter<PinnedNotesRVAdapter.PinnedViewHolder> {
    private List<Notes> pinnedNotes;
    boolean isTestData = false;
    private Activity activity;

    public PinnedNotesRVAdapter(List<Notes> pinnedNotes,Activity activity) {
        this.pinnedNotes = pinnedNotes;
        this.activity = activity;
    }

    public class PinnedViewHolder extends RecyclerView.ViewHolder {
        private TextView notesTitle;
        private TextView locationTxt;
        private TextView notesEditedTime;
        private TextView notesEditedDate;
        public PinnedViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.pinned_notes_title);
            locationTxt = itemView.findViewById(R.id.address_txt);
            notesEditedTime = itemView.findViewById(R.id.pinned_notes_time);
            notesEditedDate = itemView.findViewById(R.id.pinned_notes_date);
        }
    }
    @NonNull
    @Override
    public PinnedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pinned_rc_items, parent, false);
        PinnedViewHolder vh = new PinnedViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PinnedViewHolder holder, final int position) {
        if(!isTestData) {
            //holder.locationTxt.setText(pinnedNotes.get(position).getAddress());
            holder.notesTitle.setText(pinnedNotes.get(position).getNoteTitle());
            try {
                String lastEditedTimeStamp = null;
                if(pinnedNotes.get(position).getLastEditedTimeStamp()!=null) {
                    lastEditedTimeStamp = pinnedNotes.get(position).getLastEditedTimeStamp();
                } else {
                    lastEditedTimeStamp = pinnedNotes.get(position).getNoteCreatedTimeStamp();
                }
                Date editedDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(lastEditedTimeStamp);
                holder.notesEditedDate.setText(AppUtils.getDatefromTimeStamp(editedDate));
                holder.notesEditedTime.setText(AppUtils.getTimefromTimeStamp(editedDate));
            } catch(ParseException e) {
                e.printStackTrace();
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pinnedNotes!=null) {
                        Intent i=new Intent(activity.getApplicationContext(), AddNotesActivity.class);
                        String selectedNote = new Gson().toJson(pinnedNotes.get(position));
                        i.putExtra("selectedNote",selectedNote);
                        i.putExtra("fromPinnedNotes","Y");
                        activity.startActivityForResult(i,1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(isTestData) {
            return 3;
        }
        if(pinnedNotes!=null && !pinnedNotes.isEmpty()) {
            return pinnedNotes.size();
        } else {
            return 0;
        }
    }

    public void setPinnedNotesList(List<Notes> pinnedNotesList) {
        this.pinnedNotes = pinnedNotesList;
        notifyDataSetChanged();
    }
}
