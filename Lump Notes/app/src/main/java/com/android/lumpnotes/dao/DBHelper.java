package com.android.lumpnotes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.models.NotesAudio;
import com.android.lumpnotes.models.NotesImage;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "LumpNotes.db";
    SQLiteDatabase db;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null , 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table category (CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY_NAME TEXT, CATEGORY_ICON TEXT)");
        db.execSQL("create table note_images (IMAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGE_PATH TEXT)");
        db.execSQL("create table note_audios (AUDIO_ID INTEGER PRIMARY KEY AUTOINCREMENT, AUDIO_PATH TEXT)");
        db.execSQL("create table notes (NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY_ID INTEGER, NOTE_TITLE TEXT, " +
                "NOTE_AUDIO_ID INTEGER, NOTE_IMAGE_ID INTEGER, NOTE_DESCRIPTION TEXT, NOTE_CREATED_TIMESTAMP DATE," +
                "NOTE_LATITUDE_LOC TEXT, NOTE_LONGITUDE_LOC TEXT, IS_PINNED TEXT,IS_DELETED TEXT," +
                "CONSTRAINT fk_images FOREIGN KEY (NOTE_IMAGE_ID) REFERENCES note_images(IMAGE_ID)," +
                "CONSTRAINT fk_audios FOREIGN KEY (NOTE_AUDIO_ID) REFERENCES note_audios(AUDIO_ID)," +
                "CONSTRAINT fk_category FOREIGN KEY (CATEGORY_ID) REFERENCES category(CATEGORY_ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean saveCategories(String name, String icon) {
        ContentValues values = new ContentValues();
        values.put("CATEGORY_NAME",name);
        values.put("CATEGORY_ICON",icon);
        return db.insert("category",null, values) > 0 ? true:false;
    }

    public boolean saveNotes(int category_id,String title,String description, String latitude_loc, String longitude_loc) {
        ContentValues values = new ContentValues();
        values.put("CATEGORY_ID",category_id);
        values.put("NOTE_TITLE",title);
        values.put("NOTE_DESCRIPTION",description);
        values.put("NOTE_LATITUDE_LOC",latitude_loc);
        values.put("NOTE_LONGITUDE_LOC",longitude_loc);
        return db.insert("notes",null, values) > 0 ? true:false;
    }

    public void addNoteAudios(List<NotesAudio> audioList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (NotesAudio audio : audioList) {
                values.put("AUDIO_PATH", audio.getAudioPath());
                db.insert("note_audios", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addNoteImages(List<NotesImage> imageList) {
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (NotesImage image : imageList) {
                values.put("IMAGE_PATH", image.getImagePath());
                db.insert("note_images", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteNotes(List<Notes> notes) {
        db.beginTransaction();
        try {
            for (Notes deleteNotes : notes) {
                db.delete("notes", "note_id = ?", new String[]{""+deleteNotes.getNoteId()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void fetchWholeDataFromDb() {

    }

}
