package com.android.lumpnotes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.CategoryIconsAdapter;
import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.listeners.DialogFragmentActivityListener;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryDialogFrag extends DialogFragment implements TextWatcher {
    Button doneBtn;
    String selectedCategoryIcon;
    Context contextObj;
    CategoryRVAdapter adapter;
    List<Category> categoryList;
    boolean isEditCategory;
    Category editCategoryObj;
    DialogFragmentActivityListener listener;

    public AddCategoryDialogFrag(Context contextObj, CategoryRVAdapter adapter, List<Category> categoryList, boolean isEditCategory,
                                 Category editCategoryObj, DialogFragmentActivityListener listener) {
        this.adapter = adapter;
        this.contextObj = contextObj;
        this.categoryList = categoryList;
        this.isEditCategory = isEditCategory;
        this.editCategoryObj = editCategoryObj;
        this.listener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add_new_category_popup,
                container, false);
        TextView textView = view.findViewById(R.id.category_header_txt);
        if(isEditCategory) {
            textView.setText("EDIT CATEGORY");
        }
        view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final EditText addCategoryText = view.findViewById(R.id.new_category);
        addCategoryText.addTextChangedListener(this);
        doneBtn = view.findViewById(R.id.doneBtn);
        if(isEditCategory) {
            addCategoryText.setText(editCategoryObj.getCategoryName());
            selectedCategoryIcon = editCategoryObj.getCategoryIcon();
            doneBtn.setEnabled(true);
        }
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditCategory) {
                    boolean isValidCategory = true;
                    for (Category c : categoryList) {
                        if (c.getCategoryName().trim().equalsIgnoreCase(addCategoryText.getText().toString())) {
                            isValidCategory = false;
                        }
                    }
                    if (!isValidCategory) {
                        AppUtils.showToastMessage(contextObj, "Category already exists", false);
                    } else {
                        Category category = new Category();
                        if (selectedCategoryIcon != null) {
                            category.setCategoryIcon(selectedCategoryIcon);
                        } else {
                            selectedCategoryIcon = "default_category";
                            category.setCategoryIcon("default_category");
                        }
                        category.setCategoryName(addCategoryText.getText().toString());
                        categoryList.add(category);
                        AppUtils.showToastMessage(contextObj, "New category created successfully", true);
                        dismiss();
                        if(adapter!=null) {
                            adapter.notifyChangeForInsert(categoryList);
                        }
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                DBHelper dbHelper = new DBHelper(contextObj);
                                categoryList = dbHelper.saveAndFetchCategories(addCategoryText.getText().toString(), selectedCategoryIcon);
                                if(listener!=null) {
                                    listener.onNewCategoryCreation(categoryList);
                                }
                            }
                        });
                    }
                } else {
                    if(selectedCategoryIcon == null) {
                        selectedCategoryIcon = editCategoryObj.getCategoryIcon();
                    }
                    boolean isValidCategory = true;
                    for (Category c : categoryList) {
                        if (c.getCategoryName().trim().equalsIgnoreCase(addCategoryText.getText().toString())) {
                            isValidCategory = false;
                        }
                    }
                    if(!isValidCategory && selectedCategoryIcon.equalsIgnoreCase(editCategoryObj.getCategoryIcon())) {
                        AppUtils.showToastMessage(contextObj, "Category already exists", false);
                    } else if(addCategoryText.getText().toString().equalsIgnoreCase(editCategoryObj.getCategoryName()) &&
                            editCategoryObj.getCategoryIcon().equalsIgnoreCase(selectedCategoryIcon)) {
                        AppUtils.showToastMessage(contextObj, "No Changes made to the category", false);
                        dismiss();
                    } else {
                        int i = 0;
                        for(Category categoryObj:categoryList) {
                            if(categoryObj.getCategoryName().equalsIgnoreCase(editCategoryObj.getCategoryName())) {
                                categoryObj.setCategoryName(addCategoryText.getText().toString());
                                categoryObj.setCategoryIcon(selectedCategoryIcon);
                                categoryList.set(i,categoryObj);
                                break;
                            }
                            i++;
                        }
                        final int position = i;
                        AppUtils.showToastMessage(contextObj, "Category edited successfully", true);
                        dismiss();
                        List<Category> tempList = new ArrayList<>();
                        tempList.addAll(categoryList);
                        adapter.setItems(tempList);
                        //adapter.notifyDataSetChanged();
                        adapter.notifyChangeForEdit(position);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                DBHelper dbHelper = new DBHelper(contextObj);
                                dbHelper.updateCategoryName(editCategoryObj.getCategoryId(), addCategoryText.getText().toString(),selectedCategoryIcon);
                            }
                        });
                    }
                }
            }
        });
        final GridView gridView = view.findViewById(R.id.icon_view);
        final CategoryIconsAdapter adapter = new CategoryIconsAdapter(getResources());
        gridView.setAdapter(adapter);

        if(isEditCategory) {
            if(editCategoryObj.getCategoryIcon()!=null
                    && !editCategoryObj.getCategoryIcon().equalsIgnoreCase("default_category")) {
                adapter.selectedItemIndex = Integer.parseInt(editCategoryObj.getCategoryIcon().split("_")[2]);
                adapter.selectedItemIndex -= 1;
                selectedCategoryIcon = editCategoryObj.getCategoryIcon();
                adapter.notifyDataSetChanged();
            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.icon_view).setBackgroundResource(R.drawable.selected_btn_view);
                adapter.selectedItemIndex = position;
                selectedCategoryIcon = "ctrgy_ic_"+(position+1);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s != null && s.toString().trim().length() > 0) {
            doneBtn.setEnabled(true);
        } else {
            doneBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
