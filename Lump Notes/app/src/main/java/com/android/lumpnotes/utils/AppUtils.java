package com.android.lumpnotes.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lumpnotes.R;

public class AppUtils {
    public static void loadNotesForCategory() {

    }

    public static void loadAllCategories() {

    }

    public static void showToastMessage(Context context, String message, boolean isSuccessImg) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) ((Activity) context).findViewById(R.id.toast_layout_root));
        ImageView toastImg = layout.findViewById(R.id.toastImg);

        if(isSuccessImg) {
            toastImg.setImageResource(R.drawable.success_toast_img);
        } else {
            toastImg.setImageResource(R.drawable.error);
        }

        TextView toastMsg = layout.findViewById(R.id.toast_msg);
        toastMsg.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
