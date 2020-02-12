package com.android.lumpnotes.models;

import java.util.Date;
import java.util.List;

public class Notes {
    private int noteId;
    private int categoryId;
    private String noteTitle;
    private String noteDescription;
    private Date noteCreatedTimeStamp;
    private Date lastEditedTimeStamp;
    private String noteLatitudeLoc;
    private String noteLongitudeLoc;
    private List<NotesAudio> audioList;
    private List<NotesImage> imageList;
    private boolean isDeleted;
    private boolean isPinned;
    private Date deletedDate;
    private Date pinnedDate;
    private String address;

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

    public Date getNoteCreatedTimeStamp() {
        return noteCreatedTimeStamp;
    }

    public void setNoteCreatedTimeStamp(Date noteCreatedTimeStamp) {
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

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Date getPinnedDate() {
        return pinnedDate;
    }

    public void setPinnedDate(Date pinnedDate) {
        this.pinnedDate = pinnedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getLastEditedTimeStamp() {
        return lastEditedTimeStamp;
    }

    public void setLastEditedTimeStamp(Date lastEditedTimeStamp) {
        this.lastEditedTimeStamp = lastEditedTimeStamp;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "noteId=" + noteId +
                ", categoryId=" + categoryId +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteDescription='" + noteDescription + '\'' +
                ", noteCreatedTimeStamp='" + noteCreatedTimeStamp + '\'' +
                ", lastEditedTimeStamp='" + lastEditedTimeStamp + '\'' +
                ", noteLatitudeLoc='" + noteLatitudeLoc + '\'' +
                ", noteLongitudeLoc='" + noteLongitudeLoc + '\'' +
                ", audioList=" + audioList +
                ", imageList=" + imageList +
                ", isDeleted=" + isDeleted +
                ", isPinned=" + isPinned +
                ", deletedDate=" + deletedDate +
                ", pinnedDate=" + pinnedDate +
                ", address='" + address + '\'' +
                '}';
    }
}
