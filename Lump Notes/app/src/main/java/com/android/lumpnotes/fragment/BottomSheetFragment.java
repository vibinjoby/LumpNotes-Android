package com.android.lumpnotes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.FragmentManager;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.BottomSelectionLVAdapter;
import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.models.Category;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    FragmentManager fragManager;
    List<Category> categoryList;
    CategoryRVAdapter categoryRVAdapter;
    public BottomSheetFragment(FragmentManager fragManager,CategoryRVAdapter categoryRVAdapter,List<Category> categoryList) {
        this.fragManager = fragManager;
        this.categoryRVAdapter = categoryRVAdapter;
        this.categoryList = categoryList;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_fragment, container, false);
        ListView selectionListView = view.findViewById(R.id.selectionList);
        String[] selectionList = {"New Category","New Note"};
        BottomSelectionLVAdapter adapter = new BottomSelectionLVAdapter(selectionList,this.getContext(),fragManager);
        selectionListView.setAdapter(adapter);

        selectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    dismiss();
                    AddCategoryDialogFrag dialog = new AddCategoryDialogFrag(getContext(),categoryRVAdapter,categoryList);
                    dialog.show(fragManager,dialog.getTag());
                }
            }
        });
        view.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
