package com.android.lumpnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.adapters.HorizontalSpaceItemDecoration;
import com.android.lumpnotes.adapters.NotesRVAdapter;
import com.android.lumpnotes.fragment.BottomSheetFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView categoryRV;
    private RecyclerView.Adapter mAdapter;

    private RecyclerView notesRv;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String myDataset[] = {"vibin","shopping","travel","had","adhjad"};

        categoryRV = findViewById(R.id.category_rv);
        addButton = findViewById(R.id.addBtn);
        categoryRV.setHasFixedSize(true);

        notesRv = findViewById(R.id.notes_rv);
        notesRv.setHasFixedSize(true);

        mAdapter = new CategoryRVAdapter(myDataset);
        categoryRV.setAdapter(mAdapter);

        mAdapter = new NotesRVAdapter(myDataset);
        notesRv.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(this);
        notesRv.setLayoutManager(layoutManager);

        HorizontalSpaceItemDecoration itemDecoration = new HorizontalSpaceItemDecoration(50);
        categoryRV.addItemDecoration(itemDecoration);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }
}
