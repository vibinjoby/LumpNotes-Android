package com.android.lumpnotes.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.BottomSelectionLVAdapter;
import com.android.lumpnotes.listeners.ImageUploadClickListener;
import com.android.lumpnotes.utils.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ImageSourceBSFrag extends BottomSheetDialogFragment {
    private static final int REQUEST_GALLERY_ACCESS_PERMISSION = 8435;
    private ImageUploadClickListener listener;
    public ImageSourceBSFrag(ImageUploadClickListener listener) {
        this.listener = listener;
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
        TextView tView = view.findViewById(R.id.headerTxt);
        tView.setText("Choose Source for the image");
        String[] selectionArr = {"From gallery","Capture Image"};
        BottomSelectionLVAdapter adapter = new BottomSelectionLVAdapter(selectionArr,this.getContext(),getFragmentManager(),true);
        selectionListView.setAdapter(adapter);

        selectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    if(ActivityCompat.checkSelfPermission(getContext(),READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
                                REQUEST_GALLERY_ACCESS_PERMISSION);
                    } else {
                        dismiss();
                        if (listener != null) {
                            listener.onImageSourceSelection(false);
                        }
                    }
                } else {
                    dismiss();
                    if(listener!=null) {
                        listener.onImageSourceSelection(true);
                    }
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GALLERY_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dismiss();
                    if(listener!=null) {
                        listener.onImageSourceSelection(false);
                    }
                } else {
                    AppUtils.showToastMessage(getContext(),"Gallery Access permission denied",false);
                }
        }
    }
}
