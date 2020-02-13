package com.android.lumpnotes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lumpnotes.R;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {

    Button backBtn;
    Button locationBtn,shareBtn,pinnedBtn,deleteBtn;
    Button imageUploadBtn, audioBtn;
    EditText notesTitle,notesDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_layout);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_button) {
            finish();
        }
    }
}
