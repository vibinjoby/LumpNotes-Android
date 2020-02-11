package com.android.lumpnotes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.models.NotesAudio;
import com.android.lumpnotes.models.NotesImage;

import java.util.ArrayList;
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
        db.execSQL("create table note_images (IMAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGE_PATH TEXT,NOTE_ID INTEGER," +
                "CONSTRAINT fk_images FOREIGN KEY (NOTE_ID) REFERENCES notes(NOTE_ID))");
        db.execSQL("create table note_audios (AUDIO_ID INTEGER PRIMARY KEY AUTOINCREMENT, AUDIO_PATH TEXT,NOTE_ID INTEGER," +
                "CONSTRAINT fk_audios FOREIGN KEY (NOTE_ID) REFERENCES notes(NOTE_ID))");
        db.execSQL("create table notes (NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY_ID INTEGER, NOTE_TITLE TEXT, " +
                "NOTE_AUDIO_ID INTEGER, NOTE_IMAGE_ID INTEGER, NOTE_DESCRIPTION TEXT, NOTE_CREATED_TIMESTAMP DATE," +
                "NOTE_LATITUDE_LOC TEXT, NOTE_LONGITUDE_LOC TEXT, IS_PINNED TEXT,IS_DELETED TEXT," +
                "CONSTRAINT fk_category FOREIGN KEY (CATEGORY_ID) REFERENCES category(CATEGORY_ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CATEGORY");
        onCreate(db);
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

    public void updateCategoryName(int categoryId,String newCategoryName,String categoryIcon) {
        ContentValues values = new ContentValues();
        values.put("CATEGORY_NAME",newCategoryName);
        values.put("CATEGORY_ICON",categoryIcon);
        db.update("category",values,"category_id = ?",new String[]{""+categoryId});
    }

    public void deleteCategory(int categoryId) {
        db.delete("category", "category_id = ?", new String[]{""+categoryId});
    }

    public List<Category> fetchAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String query = "SELECT * FROM CATEGORY";
        Cursor c = null;
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()){
                do {
                    Category category = new Category();
                    category.setCategoryId(c.getInt(0));
                    category.setCategoryName(c.getString(1));
                    category.setCategoryIcon(c.getString(2));

                    categoryList.add(category);
                } while(c.moveToNext());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
            }
        }
        return categoryList;
    }

}
