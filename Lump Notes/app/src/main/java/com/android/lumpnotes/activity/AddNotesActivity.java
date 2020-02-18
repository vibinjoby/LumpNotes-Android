package com.android.lumpnotes.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.AddNotesRVAdapter;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.fragment.ChooseCategoryDialogFrag;
import com.android.lumpnotes.fragment.ImageSourceBSFrag;
import com.android.lumpnotes.fragment.MapDialogFrag;
import com.android.lumpnotes.listeners.DialogFragmentActivityListener;
import com.android.lumpnotes.listeners.ImageUploadClickListener;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.models.NotesAudio;
import com.android.lumpnotes.models.NotesImage;
import com.android.lumpnotes.utils.AppUtils;
import com.android.lumpnotes.utils.RealFilePath;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener, DialogFragmentActivityListener, ImageUploadClickListener {

    Button backBtn;
    Button locationBtn, shareBtn, pinnedBtn, saveButton;
    Button imageUploadBtn, audioBtn;
    EditText notesTitle;
    int selectedCategoryPos = -1;
    boolean isEditNote = false;
    boolean isInserted = false;
    Notes notes = null;
    boolean isPinned = false;
    boolean isFromPinnedPage = false;
    boolean isNewCategoryCreated = false;
    double longitude;
    double latitude;

    List<Category> categoryList = null;

    int counter = 0;
    private final int pick_image = 12345;
    private final int take_image = 6352;
    private MediaRecorder mRecorder;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    private static final int REQUEST_MAP_ACCESS_PERMISSION = 123;
    private Bitmap bitmap;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private String audioFileName;
    List hybridList = new ArrayList();
    AddNotesRVAdapter adapter;
    RecyclerView addNotesRV;
    ImageView dotImg;
    TextView audioRecorderText;
    int audioTimerCount = 0;
    Button cancelRecording, saveRecording;
    Timer timer;
    ImageUploadClickListener listener;
    MapDialogFrag dialogFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_layout);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_MAP_ACCESS_PERMISSION);
        }
        listener = this;

        if (getIntent().getExtras() != null && getIntent().getExtras().get("fromPinnedNotes") != null) {
            isFromPinnedPage = true;
        }

        //Getting addIntent
        if (getIntent().getExtras().getString("isAddNote") != null) {
            categoryList = new DBHelper(this).fetchAllCategories();
        }
        // Getting data for intent
        if (getIntent().getExtras().getString("selectedNote") != null) {
            String selectedNote = getIntent().getExtras().getString("selectedNote");
            notes = new Gson().fromJson(selectedNote, Notes.class);
            if (notes.getHybridList() != null) {
                hybridList = AppUtils.fetchDeserializedHybridList(notes.getHybridList());
            }
        }

        if (categoryList != null) {
            ChooseCategoryDialogFrag dialog = new ChooseCategoryDialogFrag(categoryList, null, this);
            dialog.show(getSupportFragmentManager(), dialog.getTag());
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

        if (notes != null) {
            notesTitle.setText(notes.getNoteTitle());
            isEditNote = true;
            if (notes.isPinned().equalsIgnoreCase("Y")) {
                isPinned = true;
                findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.pinned_selected);
            } else {
                isPinned = false;
                findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.bookmark);
            }
        }

        //Notes content
        findViewById(R.id.image_upload_button).setOnClickListener(this);
        findViewById(R.id.mic_button).setOnClickListener(this);
        dotImg = findViewById(R.id.dot_image);
        audioRecorderText = findViewById(R.id.audio_timer_txt);
        addNotesRV = findViewById(R.id.recycler_id);
        saveRecording = findViewById(R.id.save_rec);
        cancelRecording = findViewById(R.id.cancel_rec);
        saveRecording.setOnClickListener(this);
        cancelRecording.setOnClickListener(this);
        if (!isEditNote) {
            hybridList.add("");
        }
        adapter = new AddNotesRVAdapter(hybridList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        addNotesRV.setLayoutManager(layoutManager);
        addNotesRV.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (isEditNote) {
            if (!notes.getNoteTitle().equalsIgnoreCase(notesTitle.getText().toString()) ||
                    (hybridList.size() != notes.getHybridList().size())
            ) {
                new AlertDialog.Builder(this)
                        .setTitle("Unsaved Changes")
                        .setMessage("Are you sure you want to discard all the changes??")

                        .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (mRecorder != null) {
                                    mRecorder.stop();
                                    mRecorder.release();
                                    mRecorder = null;
                                }
                                finish();
                            }
                        })
                        .setNegativeButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper dbHelper = new DBHelper(AddNotesActivity.this);
                                String jsonObj = new Gson().toJson(hybridList);
                                isInserted = dbHelper.editNotes(notes.getNoteId(), notesTitle.getText().toString(),
                                        isPinned, "" + latitude, "" + longitude, jsonObj);
                                final Intent data = new Intent();
                                data.putExtra("category_id", selectedCategoryPos);
                                if (isFromPinnedPage) {
                                    data.putExtra("fromPinnedNotes", "Y");
                                }
                                setResult(Activity.RESULT_OK, data);
                                AppUtils.showToastMessage(AddNotesActivity.this, "Notes edited Successfully", true);
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                finish();
            }
        } else {
            if (isNewCategoryCreated) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                if (mRecorder != null) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_button) {
            onBackPressed();
        } else if (v.getId() == R.id.save_button) {
            if (notesTitle.getText() != null && !notesTitle.getText().toString().isEmpty()) {
                int categoryId = -1;
                DBHelper dbHelper = new DBHelper(this);
                //Identify the untitled category's index in the list
                if (isEditNote) {
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
                if (isEditNote) {
                    String jsonObj = new Gson().toJson(hybridList);
                    isInserted = dbHelper.editNotes(notes.getNoteId(), notesTitle.getText().toString(),
                            isPinned, "" + latitude, "" + longitude, jsonObj);
                } else {
                    String jsonObj = new Gson().toJson(hybridList);
                    isInserted = dbHelper.saveNotes(categoryId, notesTitle.getText().toString(),
                            isPinned, "" + latitude, "" + longitude, jsonObj);
                }
                if (isInserted) {
                    final Intent data = new Intent();
                    data.putExtra("category_id", selectedCategoryPos);
                    if (isFromPinnedPage) {
                        data.putExtra("fromPinnedNotes", "Y");
                    }
                    setResult(Activity.RESULT_OK, data);
                    if (categoryList != null && selectedCategoryPos < categoryList.size() && selectedCategoryPos != -1) {
                        if (isEditNote) {
                            AppUtils.showToastMessage(this, "Notes edited Successfully in the " + categoryList.get(selectedCategoryPos).getCategoryName() + " Category", true);
                        } else {
                            AppUtils.showToastMessage(this, "Notes Saved Successfully in the " + categoryList.get(selectedCategoryPos).getCategoryName() + " Category", true);
                        }
                    } else {
                        if (isEditNote) {
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
        } else if (v.getId() == R.id.bookmark_button) {
            if (isPinned) {
                v.findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.bookmark);
            } else {
                v.findViewById(R.id.bookmark_button).setBackgroundResource(R.drawable.pinned_selected);
            }
            isPinned = !isPinned;
        } else if (v.getId() == R.id.image_upload_button) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_ACCESS_PERMISSION);
            } else {
                showPictureDialog();
            }
        } else if (v.getId() == R.id.mic_button) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ActivityCompat.checkSelfPermission(this, RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{RECORD_AUDIO,WRITE_EXTERNAL_STORAGE},
                        REQUEST_AUDIO_PERMISSION_CODE);
            } else {
                showAudioFunctionality();
            }
        } else if (v.getId() == R.id.save_rec) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            audioTimerCount = 0;
            // stop the recording
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                counter++;
                NotesAudio audio = new NotesAudio();
                audio.setAudioPath(audioFileName);
                hybridList.add(audio);
                adapter.setHybridList(hybridList);
            }
            findViewById(R.id.audio_recording_layout).setVisibility(View.GONE);
            findViewById(R.id.mic_button).setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.cancel_rec) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            //Cancel the recording
            audioTimerCount = 0;
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
            findViewById(R.id.audio_recording_layout).setVisibility(View.GONE);
            findViewById(R.id.mic_button).setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.location_button) {
            //Show toast message if map permission is denied
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                AppUtils.showToastMessage(this, "Map location permission denied", false);
            } else {
                if (dialogFrag == null) {
                    if (isEditNote) {
                        try {
                            dialogFrag = new MapDialogFrag(Double.parseDouble(notes.getNoteLongitudeLoc()), Double.parseDouble(notes.getNoteLatitudeLoc()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        dialogFrag = new MapDialogFrag(0, 0);
                    }
                }
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick_image) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    NotesImage image = new NotesImage();
                    image.setImagePath(RealFilePath.getPath(this, data.getData()));
                    hybridList.add(image);
                    adapter.setHybridList(hybridList);
                    addNotesRV.scrollToPosition(addNotesRV.getBottom());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == take_image) {
            if (resultCode == Activity.RESULT_OK) {
                NotesImage image = new NotesImage();
                String imgName = "img_" + Calendar.getInstance().getTimeInMillis();
                String imagePath = this.getFilesDir() + "/" + imgName + ".jpg";
                image.setImagePath(imagePath);
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                AppUtils.persistImage(this, bitmap, imgName);
                hybridList.add(image);
                adapter.setHybridList(hybridList);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPictureDialog();
                }
                break;
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (permissionToRecord && permissionToStore) {
                        showAudioFunctionality();
                    } else {
                        AppUtils.showToastMessage(getApplicationContext(),"Audio Permission denied",false);
                    }
                }
                break;
            case REQUEST_MAP_ACCESS_PERMISSION:
                findLocation();
                break;
            case take_image:
                getImageFromCamera();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void getImageFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, take_image);
        }
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), pick_image);
        }
    }

    private void showPictureDialog() {
        ImageSourceBSFrag fragment = new ImageSourceBSFrag(listener);
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    @Override
    public void onImageSourceSelection(boolean isCamera) {
        if (isCamera) {
            getImageFromCamera();
        } else {
            getImageFromGallery();
        }
    }

    private void findLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            try {
                                //longitude and latitude
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                                if (notes == null) {
                                    notes = new Notes();
                                }
                                notes.setNoteLongitudeLoc("" + location.getLongitude());
                                notes.setNoteLatitudeLoc("" + location.getLatitude());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void showAudioFunctionality() {
        findViewById(R.id.audio_recording_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.mic_button).setVisibility(View.GONE);
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);
        fadeIn.setRepeatMode(Animation.RESTART);
        fadeIn.setRepeatCount(Animation.INFINITE);
        dotImg.setAnimation(fadeIn);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audioRecorderText.setText("0:" + audioTimerCount);
                        audioTimerCount++;
                    }
                });
            }
        }, 0, 1000);
        audioFileName = "/audio_" + Calendar.getInstance().getTimeInMillis() + ".3gp";
        mRecorder = AppUtils.startRecording(mRecorder, this, this, audioFileName);
    }
}
