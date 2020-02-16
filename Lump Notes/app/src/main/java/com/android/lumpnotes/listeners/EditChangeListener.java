package com.android.lumpnotes.listeners;

import com.android.lumpnotes.models.Category;

import java.util.List;

public interface EditChangeListener {
    void onEditChange(List<Category> categoryList);
}
