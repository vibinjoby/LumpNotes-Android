package com.android.lumpnotes.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.activity.MainActivity;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.utils.AppUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrashNotesRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Notes> deleteNotes;
    public List<Category> categoryList;
    boolean isTestData = false;
    int SMALL_VIEW_TYPE = 1;
    int LARGE_VIEW_TYPE = 2;
    private Context context;
    private View emptyDeleteView;

    public TrashNotesRVAdapter(List<Notes> deleteNotes,List<Category> categoryList,View emptyDeleteView,Context context) {
        this.deleteNotes = deleteNotes;
        this.categoryList = categoryList;
        this.context = context;
        this.emptyDeleteView = emptyDeleteView;
    }

    public class SmallNotesViewHolder extends RecyclerView.ViewHolder {
        private TextView notesTitle;
        private Button recoverButton;
        private Button deleteButton;
        public SmallNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.small_title_txt);
            recoverButton = itemView.findViewById(R.id.recover_button);
            deleteButton = itemView.findViewById(R.id.delete_note_trash_btn);
        }
    }

    public class LargeNotesViewHolder extends RecyclerView.ViewHolder{
        private TextView notesTitle;
        private Button recoverButton;
        private Button deleteButton;
        public LargeNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.large_title_txt);
            recoverButton = itemView.findViewById(R.id.recover_button);
            deleteButton = itemView.findViewById(R.id.delete_note_trash_btn);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == SMALL_VIEW_TYPE){
            ((SmallNotesViewHolder)holder).notesTitle.setText(deleteNotes.get(position).getNoteTitle());
            ((SmallNotesViewHolder)holder).deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickAction(position);
                }
            });
            ((SmallNotesViewHolder)holder).recoverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecoverClickAction(position);
                }
            });
        } else {
            ((LargeNotesViewHolder)holder).notesTitle.setText(deleteNotes.get(position).getNoteTitle());
            ((LargeNotesViewHolder)holder).deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickAction(position);
                }
            });
            ((LargeNotesViewHolder)holder).recoverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecoverClickAction(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(isTestData) {
            return 19;
        }
        if(deleteNotes!=null && !deleteNotes.isEmpty()) {
            emptyDeleteView.setVisibility(View.GONE);
            return deleteNotes.size();
        } else {
            emptyDeleteView.setVisibility(View.VISIBLE);
        }
        return 0;
    }

    private void onDeleteClickAction(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("Are you sure you want to permanently delete this note ?");
        builder.setMessage("This will be deleted immediately.You can’t undo this action.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            List<Notes> toBeDeleted = new ArrayList<>();
                            toBeDeleted.add(deleteNotes.get(position));
                            new DBHelper(context).deleteNotes(toBeDeleted);
                            deleteNotes.remove(deleteNotes.get(position));
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            AppUtils.showToastMessage(context, "Selected Note is deleted from trash", true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppUtils.showToastMessage(context,e.getMessage(),false);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create();
        builder.show();
    }

    private void onRecoverClickAction(int position) {
        boolean isCategoryStillAvailable = false;
        boolean isUntitledCategoryPresent = false;
        DBHelper dbHelper = null;
        try {
            //update the is deleted to N
            deleteNotes.get(position).setDeleted("N");
            int deletedCategoryId = deleteNotes.get(position).getCategoryId();
            int deletedNoteId = deleteNotes.get(position).getNoteId();
            String recoveredCategoryName = null;

            //Check if the category of the deleted note is still available
            for(Category category:categoryList) {
                if(category.getCategoryId() == deletedCategoryId) {
                    recoveredCategoryName = category.getCategoryName();
                    isCategoryStillAvailable = true;
                    break;
                }
            }
            //If the category is still available add the notes to the list
            if(isCategoryStillAvailable) {
                Iterator<Category> iterator = categoryList.iterator();

                while (iterator.hasNext()) {
                    Category category = iterator.next();

                    if (category.getCategoryId() == deletedCategoryId) {
                        if(category.getNotesList()!=null) {
                            category.getNotesList().add(deleteNotes.get(position));
                        } else {
                            category.setNotesList(new ArrayList<Notes>());
                            category.getNotesList().add(deleteNotes.get(position));
                        }
                        break;
                    }
                }
            } else {
                //Step-1 check if the untitled category is present
                recoveredCategoryName = "untitled";
                Iterator<Category> iterator = categoryList.iterator();

                while (iterator.hasNext()) {
                    Category ctgry = iterator.next();

                    if ("untitled".equalsIgnoreCase(ctgry.getCategoryName())) {
                        if(ctgry.getNotesList() == null) {
                            ctgry.setNotesList(new ArrayList<Notes>());
                        }
                        ctgry.getNotesList().add(deleteNotes.get(position));
                        isUntitledCategoryPresent = true;
                        deletedCategoryId = ctgry.getCategoryId();
                        break;
                    }
                }
                //Step-2 If the untitled category is not present we need to create one
                if(!isUntitledCategoryPresent) {
                    deletedCategoryId = new DBHelper(context).createAndFetchUntitledCategoryId();
                }
                //Now move the note to untitled category
                if(dbHelper == null) {
                    dbHelper = new DBHelper(context);
                }
                dbHelper.moveNotes(deletedNoteId,deletedCategoryId);
            }
            deleteNotes.remove(deleteNotes.get(position));
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            //Update it in the database
            if(dbHelper == null) {
                dbHelper = new DBHelper(context);
            }
            dbHelper.deleteRecoverNote(deletedNoteId,"N");
            categoryList = dbHelper.fetchAllCategories();
            AppUtils.showToastMessage(context,"Note moved to "+recoveredCategoryName+" Category",true);
        } catch (Exception e) {
            AppUtils.showToastMessage(context,e.getMessage(),false);
        }
    }

    public void setDeleteNotes(List<Notes> deleteNotes) {
        this.deleteNotes = deleteNotes;
        notifyDataSetChanged();
    }
}