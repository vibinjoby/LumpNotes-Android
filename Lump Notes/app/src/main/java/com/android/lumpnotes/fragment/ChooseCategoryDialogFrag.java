package com.android.lumpnotes.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.adapters.ChooseCategoryRVAdapter;
import com.android.lumpnotes.listeners.DialogFragmentActivityListener;
import com.android.lumpnotes.listeners.RecyclerViewClickListener;
import com.android.lumpnotes.models.Category;

import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryDialogFrag  extends DialogFragment implements TextWatcher, RecyclerViewClickListener {
    private List<Category> categoryList;
    private CategoryRVAdapter categoryRVAdapter;
    private int selectedCategoryPos;
    public int ignoreCategoryPos = -1;
    private DialogFragmentActivityListener listener;
    private EditText searchBar;
    ChooseCategoryRVAdapter adapter;
    public ChooseCategoryDialogFrag(List<Category> categoryList,CategoryRVAdapter categoryRVAdapter,DialogFragmentActivityListener listener) {
        this.categoryList = categoryList;
        this.categoryRVAdapter = categoryRVAdapter;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.choose_category_popup,
                container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View emptyCategoryView = view.findViewById(R.id.default_choose_category_layout);
        searchBar = view.findViewById(R.id.dialog_search_bar);
        searchBar.addTextChangedListener(this);
        Button addCategoryBtn = view.findViewById(R.id.add_category_ch_btn);
        if(ignoreCategoryPos == -1) {
            addCategoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCategoryDialogFrag dialog = new AddCategoryDialogFrag(getContext(), categoryRVAdapter, categoryList, false, null, listener,null);
                    dialog.show(getFragmentManager(), dialog.getTag());
                    dismiss();
                }
            });
        } else {
            addCategoryBtn.setVisibility(View.GONE);
        }
        RecyclerView chooseCategoryRv = view.findViewById(R.id.choose_category_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        chooseCategoryRv.setLayoutManager(layoutManager);
        adapter = new ChooseCategoryRVAdapter(categoryList,getResources(),this);
        adapter.ignoreCategoryPos = this.ignoreCategoryPos;
        chooseCategoryRv.setAdapter(adapter);

        //Close the popup in click of X button
        view.findViewById(R.id.dismiss_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if(categoryList == null || (categoryList!=null && categoryList.isEmpty())) {
            emptyCategoryView.setVisibility(View.VISIBLE);
        }
        return view;
    }

        @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchCategory(searchBar.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        selectedCategoryPos = position;
        listener.onCategorySelection(selectedCategoryPos);
        dismiss();
    }
    private void searchCategory(String searchTxt) {
        if(categoryList!=null && !categoryList.isEmpty()) {
            List<Category> filteredCategories = new ArrayList<>();
            List<Category> tempCategoryList = new ArrayList<>();
            tempCategoryList.addAll(categoryList);
            if (searchTxt != null && !searchTxt.isEmpty()) {
                for (Category category : tempCategoryList) {
                    if(ignoreCategoryPos == -1) {
                        if (category.getCategoryName().contains(searchTxt)) {
                            filteredCategories.add(category);
                        }
                    } else {
                        if(category.getCategoryName().equalsIgnoreCase(categoryList.get(ignoreCategoryPos).getCategoryName())) {
                            continue;
                        } else if (category.getCategoryName().contains(searchTxt)) {
                            filteredCategories.add(category);
                        }
                    }
                }
                adapter.ignoreCategoryPos = -1;
                adapter.setCategoryList(filteredCategories);
            } else {
                adapter.ignoreCategoryPos = this.ignoreCategoryPos;
                adapter.setCategoryList(categoryList);
            }
        }
    }
}
