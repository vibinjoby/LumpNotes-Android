package com.android.lumpnotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.android.lumpnotes.R;

public class BottomSelectionLVAdapter extends BaseAdapter {
    String[] selectionArr;
    Context context;
    FragmentManager fragManager;
    private boolean isImageFrag;

    public BottomSelectionLVAdapter(String[] selectionArr, Context context, FragmentManager fragManager,boolean isImageFrag) {
        this.selectionArr = selectionArr;
        this.context = context;
        this.fragManager = fragManager;
        this.isImageFrag = isImageFrag;
    }

    @Override
    public int getCount() {
        return selectionArr.length;
    }

    @Override
    public Object getItem(int position) {
        return selectionArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_lv_items, null);
        TextView selectionTxt = convertView.findViewById(R.id.selectionTxt);
        selectionTxt.setText(selectionArr[position]);

        ImageView selectionImg = convertView.findViewById(R.id.selectionIC);
        if(!isImageFrag) {
            if (position == 0) {
                selectionImg.setImageResource(R.drawable.new_category_ic);
            } else {
                selectionImg.setImageResource(R.drawable.new_note_ic);
            }
        } else {
            if (position == 0) {
                selectionImg.setImageResource(R.drawable.gallery);
            } else {
                selectionImg.setImageResource(R.drawable.capture);
            }
        }
        return convertView;
    }
}
