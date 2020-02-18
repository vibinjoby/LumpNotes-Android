package com.android.lumpnotes.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.lumpnotes.R;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.models.NotesAudio;
import com.android.lumpnotes.models.NotesImage;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.android.lumpnotes.activity.AddNotesActivity.REQUEST_AUDIO_PERMISSION_CODE;

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
        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a");
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
                    Date pinnedDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(notes.getPinnedDate());
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
                    Date pinnedDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(notes.getPinnedDate());
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
                    Date pinnedDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(notes.getPinnedDate());
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

    public static int getUntitledCategoryIndex(List<Category> categoryList) {
        int categoryIndex = -1;
        for(Category category:categoryList) {
            if(category.getCategoryName().equalsIgnoreCase("untitled")) {
                categoryIndex = categoryList.indexOf(category);
            }
        }
        return categoryIndex;
    }

    public static boolean CheckPermissions(Context context) {
        int result = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;

    }

    public static void RequestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public static File persistImage(Context context,Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    public static MediaRecorder startRecording(MediaRecorder mRecorder,Activity activity,Context context,String audioFileName) {
        if (CheckPermissions(context)) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            final File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), audioFileName);
            outputFile.setWritable(true);
            mRecorder.setOutputFile(outputFile.getAbsolutePath());
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            Toast.makeText(context, "Recording Started", Toast.LENGTH_SHORT).show();
        } else {
            RequestPermissions(activity);
        }
        return mRecorder;
    }

    public static List fetchDeserializedHybridList(List dataList) {
        List hybridList = new ArrayList();
        try {
            for (Object o : dataList) {
                if (o instanceof String) {
                    hybridList.add(o);
                } else if (o instanceof LinkedTreeMap) {
                    if (((LinkedTreeMap) o).get("imagePath") != null) {
                        NotesImage image = new NotesImage();
                        image.setImageId(0);
                        image.setImagePath((String) ((LinkedTreeMap) o).get("imagePath"));
                        hybridList.add(image);
                    } else if (((LinkedTreeMap) o).get("audioPath") != null) {
                        NotesAudio audio = new NotesAudio();
                        audio.setAudioId(0);
                        audio.setAudioPath((String) ((LinkedTreeMap) o).get("audioPath"));
                        hybridList.add(audio);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hybridList;
    }

    public static boolean compareLists( List<?> l1, List<?> l2 ) {
        // make a copy of the list so the original list is not changed, and remove() is supported
        ArrayList<?> cp = new ArrayList<>( l1 );
        for ( Object o : l2 ) {
            if ( !cp.remove( o ) ) {
                return false;
            }
        }
        return cp.isEmpty();
    }
}
