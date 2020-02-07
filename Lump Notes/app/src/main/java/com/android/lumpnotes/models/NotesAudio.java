package com.android.lumpnotes.models;

public class NotesAudio {
    private int audioId;
    private String audioPath;

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    @Override
    public String toString() {
        return "NotesAudio{" +
                "audioId=" + audioId +
                ", audioPath='" + audioPath + '\'' +
                '}';
    }
}
