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
import com.android.lumpnotes.utils.AppUtils;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {

    Button backBtn;
    Button locationBtn,shareBtn,pinnedBtn,deleteBtn;
    Button imageUploadBtn, audioBtn;
    EditText notesTitle,notesDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_layout);

        // Getting data for intent
        Intent editIntent = getIntent();
        String title = editIntent.getStringExtra("notesTitle");
        String description = editIntent.getStringExtra("notesDescription");


        locationBtn = findViewById(R.id.location_button);
        shareBtn = findViewById(R.id.share_button);
        pinnedBtn = findViewById(R.id.bookmark_button);
        deleteBtn = findViewById(R.id.trash_button);
        backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        pinnedBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

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
        } else if(v.getId() == R.id.trash_button) {
            DBHelper dbHelper = new DBHelper(this);
            boolean isInserted = dbHelper.saveNotes(0,notesTitle.getText().toString(),
                    notesDescription.getText().toString(),"27.2038","77.5011");
            if(isInserted) {
                setResult(Activity.RESULT_OK);
                AppUtils.showToastMessage(this,"Notes Saved Successfully",true);
                finish();
            } else {
                setResult(Activity.RESULT_CANCELED);
                AppUtils.showToastMessage(this,"Notes failed to save",false);
                finish();
            }
        }
    }
}
