package com.android.lumpnotes.models;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EmptyNotes {
    private EditText searchTxt;
    private Button searchIcon;
    private View emptyNotesView;

    public EditText getSearchTxt() {
        return searchTxt;
    }

    public void setSearchTxt(EditText searchTxt) {
        this.searchTxt = searchTxt;
    }

    public Button getSearchIcon() {
        return searchIcon;
    }

    public void setSearchIcon(Button searchIcon) {
        this.searchIcon = searchIcon;
    }

    public View getEmptyNotesView() {
        return emptyNotesView;
    }

    public void setEmptyNotesView(View emptyNotesView) {
        this.emptyNotesView = emptyNotesView;
    }
}
