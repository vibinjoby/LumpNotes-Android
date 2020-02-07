package com.android.lumpnotes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.BottomSelectionLVAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
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
        BottomSelectionLVAdapter adapter = new BottomSelectionLVAdapter(selectionList,this.getContext());

        selectionListView.setAdapter(adapter);
        view.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
