package com.android.lumpnotes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.adapters.HorizontalSpaceItemDecoration;
import com.android.lumpnotes.adapters.NotesRVAdapter;
import com.android.lumpnotes.adapters.PinnedNotesRVAdapter;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.swipes.SwipeHelper;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.fragment.BottomSheetFragment;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.utils.AppUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView categoryRV;
    private CategoryRVAdapter categoryRVAdapter;
    private NotesRVAdapter notesRVAdapter;
    private boolean isTestData = true;

    private RecyclerView notesRv;
    private Button addButton;
    private View emptyNotesView;
    private EditText searchTxt;
    private Button searchIcon;
    private List<Category> categoryList;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryList = new DBHelper(this).fetchAllCategories();

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

        //Notes Recycler View
        notesRVAdapter = new NotesRVAdapter(this,new String[]{"firstNote"});
        notesRv.setAdapter(notesRVAdapter);
        SwipeHelper swipeHelper = new SwipeHelper(this, notesRv) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Toast deleteToast =Toast. makeText(getApplicationContext(),"Delete clicked",Toast. LENGTH_SHORT);
                                deleteToast.show();
                                System.out.println(categoryList.get(categoryRVAdapter.selectedCategory));
                                System.out.println(notesRVAdapter.getItemId(pos));
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Move",
                        0,
                        Color.parseColor("#0000cc"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Toast moveToast =Toast. makeText(getApplicationContext(),"Move clicked",Toast. LENGTH_SHORT);
                                moveToast.show();
                            }
                        }
                ));
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
        itemTouchHelper.attachToRecyclerView(notesRv);

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
                            findViewById(R.id.pinned_layout).setVisibility(View.GONE);
                            findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
                            findViewById(R.id.delete_layout).setVisibility(View.GONE);
                        } else if (item.getItemId() == R.id.pinned) {
                            showPinnedNotes();
                            item.setIcon(R.drawable.pinned_selected);
                            menu.findItem(R.id.home_menu).setIcon(R.drawable.home_menu);
                            menu.findItem(R.id.bin).setIcon(R.drawable.bin);
                            findViewById(R.id.pinned_layout).setVisibility(View.VISIBLE);
                            findViewById(R.id.main_layout).setVisibility(View.GONE);
                            findViewById(R.id.delete_layout).setVisibility(View.GONE);
                        } else {
                            item.setIcon(R.drawable.trash_selected);
                            menu.findItem(R.id.home_menu).setIcon(R.drawable.home_menu);
                            menu.findItem(R.id.pinned).setIcon(R.drawable.bookmark);
                            findViewById(R.id.pinned_layout).setVisibility(View.GONE);
                            findViewById(R.id.main_layout).setVisibility(View.GONE);
                            findViewById(R.id.delete_layout).setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
    }

    private void showPinnedNotes() {
        List<Notes> notesList = new ArrayList<>();
        List<Notes> dayWiseList = null;
        PinnedNotesRVAdapter adapter = null;

        TextView todayTxt = findViewById(R.id.todayTxt);
        TextView yesterdayTxt = findViewById(R.id.yesterdayTxt);
        TextView oldTxt = findViewById(R.id.oldTxt);

        RecyclerView todayRV = findViewById(R.id.todayRV);
        RecyclerView yesterdayRV = findViewById(R.id.yesterdayRV);
        RecyclerView oldRV = findViewById(R.id.oldRV);
        layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        todayRV.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        yesterdayRV.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        oldRV.setLayoutManager(layoutManager);


        if(isTestData) {
            adapter = new PinnedNotesRVAdapter(null);
            todayRV.setAdapter(adapter);
            yesterdayRV.setAdapter(adapter);
            oldRV.setAdapter(adapter);
        } else {

            for (Category category : categoryList) {
                if (category.getNotesList() != null) {
                    notesList.addAll(category.getNotesList());
                }
            }

            dayWiseList = AppUtils.getPinnedNotesByDay(notesList, true, false);
            if (!dayWiseList.isEmpty()) {
                adapter = new PinnedNotesRVAdapter(dayWiseList);
                todayRV.setAdapter(adapter);
            } else {
                todayRV.setVisibility(View.GONE);
                todayTxt.setVisibility(View.GONE);
            }

            dayWiseList = AppUtils.getPinnedNotesByDay(notesList, false, true);
            if (!dayWiseList.isEmpty()) {
                adapter = new PinnedNotesRVAdapter(dayWiseList);
                yesterdayRV.setAdapter(adapter);
            } else {
                yesterdayRV.setVisibility(View.GONE);
                yesterdayTxt.setVisibility(View.GONE);
            }

            dayWiseList = AppUtils.getPinnedNotesByDay(notesList, false, false);
            if (!dayWiseList.isEmpty()) {
                adapter = new PinnedNotesRVAdapter(dayWiseList);
                oldRV.setAdapter(adapter);
            } else {
                oldRV.setVisibility(View.GONE);
                oldTxt.setVisibility(View.GONE);
            }
        }
    }
}
