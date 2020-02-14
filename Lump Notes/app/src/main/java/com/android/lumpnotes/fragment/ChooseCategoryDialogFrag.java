package com.android.lumpnotes.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.adapters.ChooseCategoryRVAdapter;
import com.android.lumpnotes.models.Category;

import java.util.List;

public class ChooseCategoryDialogFrag  extends DialogFragment implements TextWatcher  {
    private List<Category> categoryList;
    private CategoryRVAdapter categoryRVAdapter;
    public ChooseCategoryDialogFrag(List<Category> categoryList,CategoryRVAdapter categoryRVAdapter) {
        this.categoryList = categoryList;
        this.categoryRVAdapter = categoryRVAdapter;
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
        TextView textView = view.findViewById(R.id.dialog_search_bar);
        textView.addTextChangedListener(this);
        Button addCategoryBtn = view.findViewById(R.id.add_category_ch_btn);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                AddCategoryDialogFrag dialog = new AddCategoryDialogFrag(getContext(),categoryRVAdapter,categoryList,false,null);
                dialog.show(getFragmentManager(),dialog.getTag());
            }
        });
        RecyclerView chooseCategoryRv = view.findViewById(R.id.choose_category_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        chooseCategoryRv.setLayoutManager(layoutManager);
        ChooseCategoryRVAdapter adapter = new ChooseCategoryRVAdapter(categoryList,getResources());
        chooseCategoryRv.setAdapter(adapter);
        return view;
    }

        @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
