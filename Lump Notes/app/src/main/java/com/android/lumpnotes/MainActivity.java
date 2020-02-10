package com.android.lumpnotes;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.adapters.HorizontalSpaceItemDecoration;
import com.android.lumpnotes.adapters.NotesRVAdapter;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.fragment.BottomSheetFragment;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.utils.AppUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView categoryRV;
    private CategoryRVAdapter categoryRVAdapter;
    private NotesRVAdapter notesRVAdapter;

    private RecyclerView notesRv;
    private Button addButton;
    private View emptyNotesView;
    private EditText searchTxt;
    private Button searchIcon;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Category> categoryList = new DBHelper(this).fetchAllCategories();

        //Initializing the elements from the view
        categoryRV = findViewById(R.id.category_rv);
        addButton = findViewById(R.id.addBtn);
        emptyNotesView = findViewById(R.id.default_note);
        searchTxt = findViewById(R.id.notes_search_txt);
        searchIcon = findViewById(R.id.notes_searchbtn);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        notesRv = findViewById(R.id.notes_rv);

        categoryRV.setHasFixedSize(false);
        notesRv.setHasFixedSize(false);

        categoryRVAdapter = new CategoryRVAdapter(categoryList, getSupportFragmentManager(),categoryRV);
        categoryRV.setAdapter(categoryRVAdapter);

        notesRVAdapter = new NotesRVAdapter(new String[]{"firstNote"});
        notesRv.setAdapter(notesRVAdapter);

        layoutManager = new LinearLayoutManager(this);
        notesRv.setLayoutManager(layoutManager);

        //Setting space between two items in the recycler view
        HorizontalSpaceItemDecoration itemDecoration = new HorizontalSpaceItemDecoration(50);
        categoryRV.addItemDecoration(itemDecoration);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(getSupportFragmentManager(),categoryRVAdapter,categoryList);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        // If there is no data in the notes update the UI accordingly
        if (categoryList.size() == 0) {
            notesRv.setVisibility(View.GONE);
            searchTxt.setVisibility(View.GONE);
            searchIcon.setVisibility(View.GONE);
            emptyNotesView.setVisibility(View.VISIBLE);
        } else {
            emptyNotesView.setVisibility(View.GONE);
        }

        // Bottom Navigation View
        final Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.home_menu).setIcon(R.drawable.home_selected);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        if (item.getItemId() == R.id.home_menu) {
                            item.setIcon(R.drawable.home_selected);
                            menu.findItem(R.id.pinned).setIcon(R.drawable.bookmark);
                            menu.findItem(R.id.bin).setIcon(R.drawable.bin);
                        } else if (item.getItemId() == R.id.pinned) {
                            item.setIcon(R.drawable.pinned_selected);
                            menu.findItem(R.id.home_menu).setIcon(R.drawable.home_menu);
                            menu.findItem(R.id.bin).setIcon(R.drawable.bin);
                        } else {
                            item.setIcon(R.drawable.trash_selected);
                            menu.findItem(R.id.home_menu).setIcon(R.drawable.home_menu);
                            menu.findItem(R.id.pinned).setIcon(R.drawable.bookmark);
                        }
                        return true;
                    }
                });
    }
}
