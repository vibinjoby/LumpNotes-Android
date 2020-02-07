package com.android.lumpnotes.models;

public class NotesImage {
    private int imageId;
    private String imagePath;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "NotesImage{" +
                "imageId=" + imageId +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
