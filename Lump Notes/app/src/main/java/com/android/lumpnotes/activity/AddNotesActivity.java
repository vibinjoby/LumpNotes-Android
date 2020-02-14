package com.android.lumpnotes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.fragment.ChooseCategoryDialogFrag;
import com.android.lumpnotes.listeners.DialogFragmentActivityListener;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.utils.AppUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener, DialogFragmentActivityListener {

    Button backBtn;
    Button locationBtn,shareBtn,pinnedBtn, saveButton;
    Button imageUploadBtn, audioBtn;
    EditText notesTitle,notesDescription;
    int selectedCategoryPos = -1;

    List<Category> categoryList = null;
    CategoryRVAdapter categoryRVAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_layout);

        //Getting addIntent
        if(getIntent().getExtras().getString("categoryList")!=null) {
            String listSerializedToJson = getIntent().getExtras().getString("categoryList");
            Category [] categoryArr = new Gson().fromJson(listSerializedToJson, Category[].class);
            categoryList = new ArrayList<>(Arrays.asList(categoryArr));
        }
        //if(addIntent.getExtras().get("categoryRVAdapter")!=null) {
          //  categoryRVAdapter = (CategoryRVAdapter)addIntent.getExtras().get("categoryRVAdapter");
        //}

        // Getting data for intent
        Intent editIntent = getIntent();
        String title = editIntent.getStringExtra("notesTitle");
        String description = editIntent.getStringExtra("notesDescription");

        if(categoryList != null ) {
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

        if(title!=null && !title.isEmpty()) {
            notesTitle.setText(title);
        }
        if(description!=null && !description.isEmpty()) {
            notesDescription.setText(description);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_button) {
            finish();
        } else if(v.getId() == R.id.save_button) {
            DBHelper dbHelper = new DBHelper(this);
            //Identify the untitled category's index in the list
            if(selectedCategoryPos == -1) {
                selectedCategoryPos = AppUtils.getUntitledCategoryIndex(categoryList);
            }
            boolean isInserted = dbHelper.saveNotes(categoryList.get(selectedCategoryPos).getCategoryId(),notesTitle.getText().toString(),
                    notesDescription.getText().toString(),"27.2038","77.5011");
            if(isInserted) {
                final Intent data = new Intent();
                data.putExtra("category_id", selectedCategoryPos);
                setResult(Activity.RESULT_OK,data);
                AppUtils.showToastMessage(this,"Notes Saved Successfully in the "+categoryList.get(selectedCategoryPos).getCategoryName()+" Category",true);
                finish();
            } else {
                setResult(Activity.RESULT_CANCELED);
                AppUtils.showToastMessage(this,"Notes failed to save",false);
                finish();
            }
        }
    }

    @Override
    public void onCategorySelection(int selectedCategoryId) {
        this.selectedCategoryPos = selectedCategoryId;
    }

    @Override
    public void onNewCategoryCreation(List<Category> updatedCategory) {
        this.categoryList = updatedCategory;
        this.selectedCategoryPos = updatedCategory.size() - 1;
    }
}
