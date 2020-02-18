package com.android.lumpnotes.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.models.NotesAudio;
import com.android.lumpnotes.models.NotesImage;

import java.io.File;
import java.util.List;

public class AddNotesRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List hybridList;
    int TEXT_ITEM = 1;
    int AUDIO_ITEM = 2;
    int IMAGE_ITEM = 3;

    public AddNotesRVAdapter(List hybridList) {
        this.hybridList = hybridList;
    }

    public class TextNotesVH extends RecyclerView.ViewHolder {
        EditText noteDescription;

        public TextNotesVH(@NonNull View itemView) {
            super(itemView);
            noteDescription = itemView.findViewById(R.id.notes_description);
        }
    }

    public class ImageNotesVH extends RecyclerView.ViewHolder {
        ImageView noteImage;
        Button deleteImage;

        public ImageNotesVH(@NonNull View itemView) {
            super(itemView);
            noteImage = itemView.findViewById(R.id.note_image);
            deleteImage = itemView.findViewById(R.id.delete_image);
        }
    }

    public class AudioNotesVH extends RecyclerView.ViewHolder implements View.OnClickListener, MediaPlayer.OnCompletionListener {
        MediaPlayer mPlayer;
        Button playBtn;
        SeekBar progressBar;
        TextView forwardTimeLabel;
        TextView remainingTimeLabel;
        int totalTime;
        String fileName;
        Button deleteAudio;

        public AudioNotesVH(@NonNull View itemView) {
            super(itemView);
            playBtn = itemView.findViewById(R.id.playBtn);
            playBtn.setOnClickListener(this);
            forwardTimeLabel = itemView.findViewById(R.id.elapsedTimeLabel);
            remainingTimeLabel = itemView.findViewById(R.id.remainingTimeLabel);
            progressBar = itemView.findViewById(R.id.positionBar);
            deleteAudio = itemView.findViewById(R.id.delete_audio);
        }

        public void initializeMediaPlayer() {
            final File outputFile = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Lumpnotes/Music", fileName);
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setLooping(false);
                mPlayer.seekTo(0);
                mPlayer.setDataSource(outputFile.getAbsolutePath());
                mPlayer.prepare();
                mPlayer.setOnCompletionListener(this);
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentPosition = msg.what;
                        // Update positionBar.
                        progressBar.setProgress(currentPosition);
                        // Update Labels.
                        String elapsedTime = createTimeLabel(currentPosition);
                        forwardTimeLabel.setText(elapsedTime);

                        String remainingTime = createTimeLabel(totalTime - currentPosition);
                        remainingTimeLabel.setText("- " + remainingTime);
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mPlayer!=null) {
                            try {
                                Message msg = new Message();
                                msg.what = mPlayer.getCurrentPosition();
                                handler.sendMessage(msg);
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                progressBar.setProgress(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );
        }

        public String createTimeLabel(int time) {
            String timeLabel = "";
            int min = time / 1000 / 60;
            int sec = time / 1000 % 60;

            timeLabel = min + ":";
            if (sec < 10) timeLabel += "0";
            timeLabel += sec;

            return timeLabel;
        }

        @Override
        public void onClick(View v) {
            if (mPlayer != null) {
                if (!mPlayer.isPlaying()) {
                    // Stopping
                    mPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.stop);
                    totalTime = mPlayer.getDuration();
                    progressBar.setMax(totalTime);
                } else {
                    // Playing
                    playBtn.setBackgroundResource(R.drawable.play);
                    mPlayer.pause();
                }
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hybridList.get(position) instanceof NotesAudio) {
            return AUDIO_ITEM;
        } else if (hybridList.get(position) instanceof NotesImage) {
            return IMAGE_ITEM;
        } else {
            return TEXT_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TEXT_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_rc_text_item, parent, false);
            TextNotesVH vh = new TextNotesVH(v);
            return vh;
        } else if (viewType == AUDIO_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notes_rc_audio, parent, false);
            AudioNotesVH vh = new AudioNotesVH(v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notes_rc_image, parent, false);
            ImageNotesVH vh = new ImageNotesVH(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TEXT_ITEM) {
            ((TextNotesVH) holder).noteDescription.setText(hybridList.get(position).toString());
            ((TextNotesVH) holder).noteDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(((TextNotesVH) holder).noteDescription.getText()!=null ) {
                        hybridList.set(position, ((TextNotesVH) holder).noteDescription.getText().toString());
                    } else {
                        hybridList.set(position, "");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (getItemViewType(position) == AUDIO_ITEM) {
            ((AudioNotesVH) holder).fileName = ((NotesAudio) hybridList.get(position)).getAudioPath();
            ((AudioNotesVH) holder).initializeMediaPlayer();
            ((AudioNotesVH) holder).deleteAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hybridList.remove(hybridList.get(position));
                    notifyItemRemoved(position);
                }
            });

        } else {
            ((ImageNotesVH)holder).deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hybridList.remove(hybridList.get(position));
                    notifyItemRemoved(position);
                }
            });
            File imgFile = new File(((NotesImage) hybridList.get(position)).getImagePath());

            if (imgFile.exists()) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ((ImageNotesVH) holder).noteImage.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return hybridList.size();
    }

    public void setHybridList(List hybridList) {
        this.hybridList = hybridList;
        notifyItemInserted(hybridList.size());
    }
}
