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
import com.android.lumpnotes.models.Notes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppUtils {
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

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isYesterday(Date date2) {
        Date todaysDate = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(todaysDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static String getTimefromTimeStamp(Date date) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = localDateFormat.format(date);
        return  time;
    }

    public static String getDatefromTimeStamp(Date date) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String time = localDateFormat.format(date);
        return  time;
    }

    public static List<Notes> getPinnedNotesByDay(List<Notes> notesList,boolean searchForToday, boolean searchForYesterday) {
        List<Notes> dayWiseList = new ArrayList<>();
        if(searchForToday) {
            for (Notes notes : notesList) {
                try {
                    Date pinnedDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(notes.getPinnedDate());
                    if (isToday(pinnedDate)) {
                        dayWiseList.add(notes);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if(searchForYesterday) {
            for (Notes notes : notesList) {
                try {
                    Date pinnedDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(notes.getPinnedDate());
                    if(isYesterday(pinnedDate)) {
                        dayWiseList.add(notes);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Notes notes : notesList) {
                try {
                    Date pinnedDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(notes.getPinnedDate());
                    if(!isToday(pinnedDate) && !isYesterday(pinnedDate)) {
                        dayWiseList.add(notes);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return dayWiseList;
    }
}
