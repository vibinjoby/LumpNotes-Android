package com.android.lumpnotes.models;

import java.util.List;

public class Notes {
    private int noteId;
    private int categoryId;
    private String noteTitle;
    private String noteDescription;
    private int audioId;
    private int imageId;
    private String noteCreatedTimeStamp;
    private String lastEditedTimeStamp;
    private String noteLatitudeLoc;
    private String noteLongitudeLoc;
    private List<NotesAudio> audioList;
    private List<NotesImage> imageList;
    private String isDeleted;
    private String isPinned;
    private String deletedDate;
    private String pinnedDate;
    private String address;
    private List hybridList;

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

    public String isDeleted() {
        return isDeleted;
    }

    public void setDeleted(String deleted) {
        isDeleted = deleted;
    }

    public String isPinned() {
        return isPinned;
    }

    public void setPinned(String pinned) {
        isPinned = pinned;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getPinnedDate() {
        return pinnedDate;
    }

    public void setPinnedDate(String pinnedDate) {
        this.pinnedDate = pinnedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastEditedTimeStamp() {
        return lastEditedTimeStamp;
    }

    public void setLastEditedTimeStamp(String lastEditedTimeStamp) {
        this.lastEditedTimeStamp = lastEditedTimeStamp;
    }

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(String isPinned) {
        this.isPinned = isPinned;
    }

    public List getHybridList() {
        return hybridList;
    }

    public void setHybridList(List hybridList) {
        this.hybridList = hybridList;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "noteId=" + noteId +
                ", categoryId=" + categoryId +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteDescription='" + noteDescription + '\'' +
                ", audioId=" + audioId +
                ", imageId=" + imageId +
                ", noteCreatedTimeStamp='" + noteCreatedTimeStamp + '\'' +
                ", lastEditedTimeStamp='" + lastEditedTimeStamp + '\'' +
                ", noteLatitudeLoc='" + noteLatitudeLoc + '\'' +
                ", noteLongitudeLoc='" + noteLongitudeLoc + '\'' +
                ", audioList=" + audioList +
                ", imageList=" + imageList +
                ", isDeleted='" + isDeleted + '\'' +
                ", isPinned='" + isPinned + '\'' +
                ", deletedDate='" + deletedDate + '\'' +
                ", pinnedDate='" + pinnedDate + '\'' +
                ", address='" + address + '\'' +
                ", hybridList=" + hybridList +
                '}';
    }
}
