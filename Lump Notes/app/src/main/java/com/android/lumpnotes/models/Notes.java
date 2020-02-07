package com.android.lumpnotes.models;

import java.util.List;

public class Notes {
    private int noteId;
    private int categoryId;
    private String noteTitle;
    private String noteDescription;
    private String noteCreatedTimeStamp;
    private String noteLatitudeLoc;
    private String noteLongitudeLoc;
    private List<NotesAudio> audioList;
    private List<NotesImage> imageList;
    private boolean isDeleted;
    private boolean isPinned;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public String getNoteCreatedTimeStamp() {
        return noteCreatedTimeStamp;
    }

    public void setNoteCreatedTimeStamp(String noteCreatedTimeStamp) {
        this.noteCreatedTimeStamp = noteCreatedTimeStamp;
    }

    public String getNoteLatitudeLoc() {
        return noteLatitudeLoc;
    }

    public void setNoteLatitudeLoc(String noteLatitudeLoc) {
        this.noteLatitudeLoc = noteLatitudeLoc;
    }

    public String getNoteLongitudeLoc() {
        return noteLongitudeLoc;
    }

    public void setNoteLongitudeLoc(String noteLongitudeLoc) {
        this.noteLongitudeLoc = noteLongitudeLoc;
    }

    public List<NotesAudio> getAudioList() {
        return audioList;
    }

    public void setAudioList(List<NotesAudio> audioList) {
        this.audioList = audioList;
    }

    public List<NotesImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<NotesImage> imageList) {
        this.imageList = imageList;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "noteId=" + noteId +
                ", categoryId=" + categoryId +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteDescription='" + noteDescription + '\'' +
                ", noteCreatedTimeStamp='" + noteCreatedTimeStamp + '\'' +
                ", noteLatitudeLoc='" + noteLatitudeLoc + '\'' +
                ", noteLongitudeLoc='" + noteLongitudeLoc + '\'' +
                ", audioList=" + audioList +
                ", imageList=" + imageList +
                ", isDeleted=" + isDeleted +
                ", isPinned=" + isPinned +
                '}';
    }
}
