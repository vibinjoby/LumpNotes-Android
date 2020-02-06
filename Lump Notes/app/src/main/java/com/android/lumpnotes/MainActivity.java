package com.android.lumpnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.adapters.HorizontalSpaceItemDecoration;
import com.android.lumpnotes.adapters.NotesRVAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView categoryRV;
    private RecyclerView.Adapter mAdapter;

    private RecyclerView notesRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String myDataset[] = {"vibin","shopping","travel","had","adhjad"};

        categoryRV = findViewById(R.id.category_rv);
        categoryRV.setHasFixedSize(true);

        notesRv = findViewById(R.id.notes_rv);
        notesRv.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter = new CategoryRVAdapter(myDataset);
        categoryRV.setAdapter(mAdapter);

        mAdapter = new NotesRVAdapter(myDataset);
        notesRv.setAdapter(mAdapter);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        notesRv.setLayoutManager(layoutManager);

        HorizontalSpaceItemDecoration itemDecoration = new HorizontalSpaceItemDecoration(50);
        categoryRV.addItemDecoration(itemDecoration);
    }
}
