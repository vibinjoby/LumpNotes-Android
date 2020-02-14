package com.android.lumpnotes.listeners;

import com.android.lumpnotes.models.Category;

import java.util.List;

public interface DialogFragmentActivityListener {
    void onCategorySelection(int selectedCategoryId);
    void onNewCategoryCreation(List<Category> updatedCategory);
}
