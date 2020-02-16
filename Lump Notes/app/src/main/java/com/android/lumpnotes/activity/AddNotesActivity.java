package com.android.lumpnotes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lumpnotes.R;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.fragment.ChooseCategoryDialogFrag;
import com.android.lumpnotes.listeners.DialogFragmentActivityListener;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.utils.AppUtils;
import com.google.gson.Gson;

import java.util.List;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener, DialogFragmentActivityListener {

    Button backBtn;
    Button locationBtn,shareBtn,pinnedBtn, saveButton;
    Button imageUploadBtn, audioBtn;
    EditText notesTitle,notesDescription;
    int selectedCategoryPos = -1;
    boolean isEditNote = false;
    boolean isInserted = false;
    Notes notes = null;
    boolean isPinned = false;
    boolean isFromPinnedPage = false;
    boolean isNewCategoryCreated = false;

    List<Category> categoryList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_layout);

        if(getIntent().getExtras()!=null && getIntent().getExtras().get("fromPinnedNotes")!=null) {
            isFromPinnedPage = true;
        }

        //Getting addIntent
        if(getIntent().getExtras().getString("isAddNote")!=null) {
            categoryList = new DBHelper(this).fetchAllCategories();
        }
        // Getting data for intent
        if(getIntent().getExtras().getString("selectedNote")!=null) {
            String selectedNote = getIntent().getExtras().getString("selectedNote");
            notes = new Gson().fromJson(selectedNote, Notes.class);
        }

        if(categoryList!=null ) {
            ChooseCategoryDialogFrag dialog = new ChooseCategoryDialogFrag(categoryList,null,this);
            dialog.show(getSupportFragmentManager(),dialog.getTag());
        }

        locationBtn = findViewById(R.id.location_button);
        shareBtn = findViewById(R.id.share_button);
        pinnedBtn = findViewById(R.id.bookmark_button);
        saveButton = findViewById(R.id.save_button);
        backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        pinnedBtn.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        imageUploadBtn = findViewById(R.id.image_upload_button);
        audioBtn = findViewById(R.id.mic_button);

        notesTitle = findViewById(R.id.notes_title);
        notesDescription = findViewById(R.id.notes_description);

        if(notes != null) {
            notesTitle.setText(notes.getNoteTitle());
            notesDescription.setText(notes.getNoteDescription());
            isEditNote = true;
            if(notes.isPinned().equalsIgnoreCase("Y")) {
                isPinned = true;
                findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.pinned_selected);
            } else {
                isPinned = false;
                findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.bookmark);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isNewCategoryCreated) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_button) {
            onBackPressed();
        } else if(v.getId() == R.id.save_button) {
            if(notesTitle.getText()!=null && !notesTitle.getText().toString().isEmpty()) {
                int categoryId = -1;
                DBHelper dbHelper = new DBHelper(this);
                //Identify the untitled category's index in the list
                if(isEditNote) {
                    categoryId = notes.getCategoryId();
                } else if (selectedCategoryPos == -1) {
                    selectedCategoryPos = AppUtils.getUntitledCategoryIndex(categoryList);
                    if (selectedCategoryPos != -1) {
                        categoryId = categoryList.get(selectedCategoryPos).getCategoryId();
                    }
                } else {
                    categoryId = categoryList.get(selectedCategoryPos).getCategoryId();
                }
                //If there is no untitled category created before we need to create a new one
                if(isEditNote) {
                    isInserted = dbHelper.editNotes(notes.getNoteId(),notesTitle.getText().toString(),
                            notesDescription.getText().toString(), isPinned,"27.2038", "77.5011");
                } else {
                    isInserted = dbHelper.saveNotes(categoryId, notesTitle.getText().toString(),
                            notesDescription.getText().toString(), isPinned,"27.2038", "77.5011");
                }
                if (isInserted) {
                    final Intent data = new Intent();
                    data.putExtra("category_id", selectedCategoryPos);
                    if(isFromPinnedPage) {
                        data.putExtra("fromPinnedNotes","Y");
                    }
                    setResult(Activity.RESULT_OK, data);
                    if(categoryList!=null && selectedCategoryPos < categoryList.size() && selectedCategoryPos!=-1) {
                        if(isEditNote) {
                            AppUtils.showToastMessage(this, "Notes edited Successfully in the " + categoryList.get(selectedCategoryPos).getCategoryName() + " Category", true);
                        } else {
                            AppUtils.showToastMessage(this, "Notes Saved Successfully in the " + categoryList.get(selectedCategoryPos).getCategoryName() + " Category", true);
                        }
                    } else {
                        if(isEditNote) {
                            AppUtils.showToastMessage(this, "Notes edited Successfully", true);
                        } else {
                            AppUtils.showToastMessage(this, "Notes Saved Successfully", true);
                        }
                    }
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    AppUtils.showToastMessage(this, "Notes failed to save", false);
                    finish();
                }
            } else {
                notesTitle.setError("Title is mandatory for saving Note");
            }
        } else if(v.getId() == R.id.bookmark_button) {
            if(isPinned) {
                v.findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.bookmark);
            } else {
                v.findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.pinned_selected);
            }
            isPinned = !isPinned;
        }
    }

    @Override
    public void onCategorySelection(int selectedCategoryId) {
        this.selectedCategoryPos = selectedCategoryId;
    }

    @Override
    public void onNewCategoryCreation(List<Category> updatedCategory) {
        isNewCategoryCreated = true;
        this.categoryList = updatedCategory;
        this.selectedCategoryPos = updatedCategory.size() - 1;
    }
}
