package com.android.lumpnotes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.Nullable;

import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.models.NotesAudio;
import com.android.lumpnotes.models.NotesImage;
import com.android.lumpnotes.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LumpNotes.db";
    SQLiteDatabase db;
    private Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
        this.context = context;
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
                "NOTE_EDITED_DATE DATE, NOTE_DELETED_DATE DATE, NOTE_PINNED_DATE DATE," +
                "NOTE_LATITUDE_LOC TEXT, NOTE_LONGITUDE_LOC TEXT, IS_PINNED TEXT,IS_DELETED TEXT,HYBRID_LIST_OBJ TEXT,ADDRESS TEXT," +
                "CONSTRAINT fk_category FOREIGN KEY (CATEGORY_ID) REFERENCES category(CATEGORY_ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CATEGORY");
        onCreate(db);
    }

    public List<Category> saveAndFetchCategories(String name, String icon) {
        List<Category> categoryList = null;
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put("CATEGORY_NAME", name);
            values.put("CATEGORY_ICON", icon);
            long insertedVal = db.insert("category", null, values);
            System.out.println(insertedVal);
            categoryList = fetchAllCategories();
            return categoryList;
        } finally {
            if (db != null) {
                db.close();
                db=null;
            }
        }
    }

    public boolean editNotes(int noteId, String title, boolean isPinned,String latitude_loc, String longitude_loc,
            String jsonObj) {
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            String currentDate = dateFormat.format(date);
            ContentValues values = new ContentValues();
            values.put("NOTE_TITLE", title);
            values.put("NOTE_LATITUDE_LOC", latitude_loc);
            values.put("NOTE_LONGITUDE_LOC", longitude_loc);
            values.put("NOTE_EDITED_DATE",currentDate);
            values.put("IS_PINNED",isPinned?"Y":"N");
            if(isPinned) {
                values.put("NOTE_PINNED_DATE",currentDate);
            }
            values.put("IS_DELETED","N");
            values.put("HYBRID_LIST_OBJ",jsonObj);
            return db.update("notes", values, "NOTE_ID="+noteId,null) > 0 ? true : false;
        } finally {
            if (db != null) {
                db.close();
                db=null;
            }
        }
    }

    public boolean saveNotes(int category_id, String title, boolean isPinned,String latitude_loc, String longitude_loc,
    String jsonObj) {
        String address = null;
        double latitude = 0,longitude = 0;
        try {
            latitude = Double.parseDouble(latitude_loc);
            longitude = Double.parseDouble(longitude_loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (category_id == -1) {
            category_id = fetchUntitledCategoryId();
        }
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = null;
            if(latitude != 0 && longitude!=0) {
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    address = addresses.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(db == null) {
                db = this.getWritableDatabase();
            }
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            String currentDate = dateFormat.format(date);
            ContentValues values = new ContentValues();
            values.put("CATEGORY_ID", category_id);
            values.put("NOTE_TITLE", title);
            values.put("NOTE_LATITUDE_LOC", latitude_loc);
            values.put("NOTE_LONGITUDE_LOC", longitude_loc);
            values.put("NOTE_CREATED_TIMESTAMP",currentDate);
            values.put("IS_PINNED",isPinned?"Y":"N");
            if(address!=null) {
                values.put("ADDRESS", address);
            }
            if(isPinned) {
                values.put("NOTE_PINNED_DATE",currentDate);
            }
            values.put("IS_DELETED","N");
            values.put("HYBRID_LIST_OBJ",jsonObj);
            return db.insert("notes", null, values) > 0 ? true : false;
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }

    public void deleteNotes(List<Notes> notes) {
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            db.beginTransaction();
            for (Notes deleteNotes : notes) {
                db.delete("notes", "note_id = ?", new String[]{"" + deleteNotes.getNoteId()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            if (db != null) {
                db.close();
                db=null;
            }
        }
    }

    public void updateCategoryName(int categoryId, String newCategoryName, String categoryIcon) {
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put("CATEGORY_NAME", newCategoryName);
            values.put("CATEGORY_ICON", categoryIcon);
            long updatedCount = db.update("category", values, "category_id = ?", new String[]{"" + categoryId});
            System.out.println(updatedCount);
        } finally {
            if(db!=null) {
                db.close();
                db=null;
            }
        }
    }

    public void deleteCategory(int categoryId) {
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            db.delete("category", "category_id = ?", new String[]{"" + categoryId});
        } finally {
            if(db!=null) {
                db.close();
                db=null;
            }
        }
    }

    public List<Category> fetchAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String query = "SELECT * FROM CATEGORY";
        Cursor c = null;
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setCategoryId(c.getInt(0));
                    category.setCategoryName(c.getString(1));
                    category.setCategoryIcon(c.getString(2));

                    categoryList.add(category);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(db!=null) {
                db.close();
                db=null;
            }
        }
        categoryList = fetchNotes(categoryList);
        return categoryList;
    }

    public List<Category> fetchNotes(List<Category> categoryList) {
        Cursor c = null;
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            for (int i = 0; i < categoryList.size(); i++) {
                String query = "SELECT * FROM NOTES WHERE category_id = ? AND IS_DELETED = ?";
                c = db.rawQuery(query, new String[]{"" + categoryList.get(i).getCategoryId(),"N"});
                if (c.moveToFirst()) {
                    do {
                        Notes notes = new Notes();
                        notes.setNoteId(c.getInt(0));
                        notes.setCategoryId(c.getInt(1));
                        notes.setNoteTitle(c.getString(2));
                        notes.setAudioId(c.getInt(3));
                        notes.setImageId(c.getInt(4));
                        notes.setNoteDescription(c.getString(5));
                        notes.setNoteCreatedTimeStamp(c.getString(6));
                        notes.setLastEditedTimeStamp(c.getString(7));
                        notes.setDeletedDate(c.getString(8));
                        notes.setPinnedDate(c.getString(9));
                        notes.setNoteLatitudeLoc(c.getString(10));
                        notes.setNoteLongitudeLoc(c.getString(11));
                        notes.setPinned(c.getString(12));
                        notes.setDeleted(c.getString(13));

                        //Getting the JSON object from DB and converting it to List
                        String jsonObj = c.getString(14);
                        if(jsonObj!=null && !jsonObj.isEmpty()) {
                            List tempList = new Gson().fromJson(jsonObj, List.class);
                            List hybridList = AppUtils.fetchDeserializedHybridList(tempList);
                            if(hybridList!=null && !hybridList.isEmpty()) {
                                notes.setHybridList(hybridList);
                            }
                        }
                        notes.setAddress(c.getString(15));
                        if (categoryList.get(i).getNotesList() == null) {
                            categoryList.get(i).setNotesList(new ArrayList<Notes>());
                        }
                        categoryList.get(i).getNotesList().add(notes);
                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
                db=null;
            }
        }
        return categoryList;
    }

    private int fetchUntitledCategoryId() {
        int categoryId = -1;
        String query = "SELECT CATEGORY_ID FROM CATEGORY WHERE CATEGORY_NAME = ?";
        Cursor c = null;
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            c = db.rawQuery(query, new String[]{"untitled"});
            if (c.moveToFirst()) {
                do {
                    categoryId = c.getInt(0);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
                db=null;
            }
        }
        if(categoryId == -1) {
            categoryId = createAndFetchUntitledCategoryId();
        }
        return categoryId;
    }
    public int createAndFetchUntitledCategoryId() {
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put("CATEGORY_NAME", "untitled");
            values.put("CATEGORY_ICON", "default_category");
            db.insert("CATEGORY", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(db!=null) {
                db.close();
                db = null;
            }
        }

        return fetchUntitledCategoryId();
    }

    public boolean deleteRecoverNote(int noteId,String value) {
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put("IS_DELETED", value);
            return db.update("notes", values, "NOTE_ID=" + noteId, null) > 0 ? true : false;
        } finally {
            if(db!=null) {
                db.close();
                db=null;
            }
        }
    }

    public List<Notes> fetchPinnedNotes() {
        List<Notes> pinnedNotesList = new ArrayList<>();
        String query = "SELECT * FROM NOTES WHERE IS_PINNED = ? AND IS_DELETED = ?";
        Cursor c = null;
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            c = db.rawQuery(query, new String[]{"Y","N"});
            if (c.moveToFirst()) {
                do {
                    Notes notes = new Notes();
                    notes.setNoteId(c.getInt(0));
                    notes.setCategoryId(c.getInt(1));
                    notes.setNoteTitle(c.getString(2));
                    notes.setAudioId(c.getInt(3));
                    notes.setImageId(c.getInt(4));
                    notes.setNoteDescription(c.getString(5));
                    notes.setNoteCreatedTimeStamp(c.getString(6));
                    notes.setLastEditedTimeStamp(c.getString(7));
                    notes.setDeletedDate(c.getString(8));
                    notes.setPinnedDate(c.getString(9));
                    notes.setNoteLatitudeLoc(c.getString(10));
                    notes.setNoteLongitudeLoc(c.getString(11));
                    notes.setPinned(c.getString(12));
                    notes.setDeleted(c.getString(13));

                    //Getting the JSON object from DB and converting it to List
                    String jsonObj = c.getString(14);
                    if(jsonObj!=null && !jsonObj.isEmpty()) {
                        List tempList = new Gson().fromJson(jsonObj, List.class);
                        List hybridList = AppUtils.fetchDeserializedHybridList(tempList);
                        if(hybridList!=null && !hybridList.isEmpty()) {
                            notes.setHybridList(hybridList);
                        }
                    }
                    notes.setAddress(c.getString(15));

                    pinnedNotesList.add(notes);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
                db=null;
            }
        }
        return pinnedNotesList;
    }

    public List<Notes> fetchDeletedNotes() {
        List<Notes> deletedNotesList = new ArrayList<>();
        String query = "SELECT * FROM NOTES WHERE IS_DELETED = ?";
        Cursor c = null;
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            c = db.rawQuery(query, new String[]{"Y"});
            if (c.moveToFirst()) {
                do {
                    Notes notes = new Notes();
                    notes.setNoteId(c.getInt(0));
                    notes.setCategoryId(c.getInt(1));
                    notes.setNoteTitle(c.getString(2));
                    notes.setAudioId(c.getInt(3));
                    notes.setImageId(c.getInt(4));
                    notes.setNoteDescription(c.getString(5));
                    notes.setNoteCreatedTimeStamp(c.getString(6));
                    notes.setLastEditedTimeStamp(c.getString(7));
                    notes.setDeletedDate(c.getString(8));
                    notes.setPinnedDate(c.getString(9));
                    notes.setNoteLatitudeLoc(c.getString(10));
                    notes.setNoteLongitudeLoc(c.getString(11));
                    notes.setPinned(c.getString(12));
                    notes.setDeleted(c.getString(13));

                    //Getting the JSON object from DB and converting it to List
                    String jsonObj = c.getString(14);
                    if(jsonObj!=null && !jsonObj.isEmpty()) {
                        List tempList = new Gson().fromJson(jsonObj, List.class);
                        List hybridList = AppUtils.fetchDeserializedHybridList(tempList);
                        if(hybridList!=null && !hybridList.isEmpty()) {
                            notes.setHybridList(hybridList);
                        }
                    }
                    notes.setAddress(c.getString(15));

                    deletedNotesList.add(notes);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
                db=null;
            }
        }
        return deletedNotesList;
    }

    public boolean moveNotes(int noteId,int destinationCategoryId) {
        try {
            if(db == null) {
                db = this.getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put("CATEGORY_ID", destinationCategoryId);
            return db.update("NOTES", values, "NOTE_ID = ?", new String[]{"" + noteId}) > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(db!=null) {
                db.close();
                db=null;
            }
        }
        return false;
    }

}
