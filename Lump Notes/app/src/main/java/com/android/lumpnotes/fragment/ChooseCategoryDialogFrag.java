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

import java.util.List;

public class ChooseCategoryDialogFrag  extends DialogFragment implements TextWatcher, RecyclerViewClickListener {
    private List<Category> categoryList;
    private CategoryRVAdapter categoryRVAdapter;
    private int selectedCategoryPos;
    public int ignoreCategoryPos = -1;
    private DialogFragmentActivityListener listener;
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
        TextView textView = view.findViewById(R.id.dialog_search_bar);
        textView.addTextChangedListener(this);
        Button addCategoryBtn = view.findViewById(R.id.add_category_ch_btn);
        if(ignoreCategoryPos == -1) {
            addCategoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCategoryDialogFrag dialog = new AddCategoryDialogFrag(getContext(), categoryRVAdapter, categoryList, false, null, listener);
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
        ChooseCategoryRVAdapter adapter = new ChooseCategoryRVAdapter(categoryList,getResources(),this);
        chooseCategoryRv.setAdapter(adapter);

        //Close the popup in click of X button
        view.findViewById(R.id.dismiss_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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

    @Override
    public void recyclerViewListClicked(View v, int position) {
        selectedCategoryPos = position;
        listener.onCategorySelection(selectedCategoryPos);
        dismiss();
    }
}
