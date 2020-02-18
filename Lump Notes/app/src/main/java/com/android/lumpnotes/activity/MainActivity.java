package com.android.lumpnotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.adapters.HorizontalSpaceItemDecoration;
import com.android.lumpnotes.adapters.NotesRVAdapter;
import com.android.lumpnotes.adapters.PinnedNotesRVAdapter;
import com.android.lumpnotes.adapters.TrashNotesRVAdapter;
import com.android.lumpnotes.fragment.ChooseCategoryDialogFrag;
import com.android.lumpnotes.listeners.DialogFragmentActivityListener;
import com.android.lumpnotes.models.EmptyNotes;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.swipes.SwipeHelper;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.fragment.PlusButtonBSFragment;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.utils.AppUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogFragmentActivityListener {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView categoryRV;
    private CategoryRVAdapter categoryRVAdapter;
    private NotesRVAdapter notesRVAdapter;

    private RecyclerView notesRv;
    private Button addButton;
    private View emptyNotesView;
    private EditText searchTxt;
    private Button searchIcon;
    private List<Category> categoryList;
    private List<Notes> pinnedNotesList;
    private Notes movableNotesObj;

    private BottomNavigationView bottomNavigationView;
    private Context context;

    PinnedNotesRVAdapter todaysAdapter = null;
    PinnedNotesRVAdapter yesterdaysAdapter = null;
    PinnedNotesRVAdapter oldAdapter = null;

    List<Notes> dayWiseList = null;

    TextView todayTxt = null;
    TextView yesterdayTxt = null;
    TextView oldTxt = null;

    RecyclerView todayRV = null;
    RecyclerView yesterdayRV = null;
    RecyclerView oldRV = null;
    DialogFragmentActivityListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        listener = this;
        categoryList = new DBHelper(this).fetchAllCategories();
        pinnedNotesList = new DBHelper(this).fetchPinnedNotes();

        //Initializing the elements from the view
        categoryRV = findViewById(R.id.category_rv);
        addButton = findViewById(R.id.addBtn);
        emptyNotesView = findViewById(R.id.default_note);
        searchTxt = findViewById(R.id.notes_search_txt);
        searchIcon = findViewById(R.id.notes_searchbtn);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        notesRv = findViewById(R.id.notes_rv);
        todayTxt = findViewById(R.id.todayTxt);
        yesterdayTxt = findViewById(R.id.yesterdayTxt);
        oldTxt = findViewById(R.id.oldTxt);

        todayRV = findViewById(R.id.todayRV);
        yesterdayRV = findViewById(R.id.yesterdayRV);
        oldRV = findViewById(R.id.oldRV);

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

        //Pinned notes data population
        dayWiseList = AppUtils.getPinnedNotesByDay(pinnedNotesList, true, false);
        if (!dayWiseList.isEmpty()) {
            todaysAdapter = new PinnedNotesRVAdapter(dayWiseList, this);
            todaysAdapter.setPinnedNotesList(dayWiseList);
            todayRV.setAdapter(todaysAdapter);
        } else {
            todayRV.setVisibility(View.GONE);
            todayTxt.setVisibility(View.GONE);
        }

        dayWiseList = AppUtils.getPinnedNotesByDay(pinnedNotesList, false, true);
        if (!dayWiseList.isEmpty()) {
            yesterdaysAdapter = new PinnedNotesRVAdapter(dayWiseList, this);
            yesterdayRV.setAdapter(yesterdaysAdapter);
        } else {
            yesterdayRV.setVisibility(View.GONE);
            yesterdayTxt.setVisibility(View.GONE);
        }

        dayWiseList = AppUtils.getPinnedNotesByDay(pinnedNotesList, false, false);
        if (!dayWiseList.isEmpty()) {
            oldAdapter = new PinnedNotesRVAdapter(dayWiseList, this);
            oldRV.setAdapter(oldAdapter);
        } else {
            oldRV.setVisibility(View.GONE);
            oldTxt.setVisibility(View.GONE);
        }

        //Initializing the model object for passing it to notes adapter
        EmptyNotes emptyNotes = new EmptyNotes();
        emptyNotes.setEmptyNotesView(emptyNotesView);
        emptyNotes.setSearchIcon(searchIcon);
        emptyNotes.setSearchTxt(searchTxt);

        categoryRV.setHasFixedSize(false);
        notesRv.setHasFixedSize(false);

        categoryRVAdapter = new CategoryRVAdapter(categoryList, getSupportFragmentManager(), categoryRV);
        categoryRV.setAdapter(categoryRVAdapter);

        //Notes Recycler View and setting the values of first category as default to notes
        if (categoryList != null && !categoryList.isEmpty()) {
            notesRVAdapter = new NotesRVAdapter(categoryList.get(0).getNotesList(), emptyNotes, this);
        } else {
            notesRVAdapter = new NotesRVAdapter(null, emptyNotes, this);
        }
        notesRv.setAdapter(notesRVAdapter);
        //Set the notes adapter obj to category RV adapter for on click event to update
        categoryRVAdapter.setNotesAdapterobj(notesRVAdapter);
        SwipeHelper swipeHelper = new SwipeHelper(this, notesRv, 60, 60) {
            @Override
            public void instantiateUnderlayButton(final RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                if (viewHolder instanceof NotesRVAdapter.NotesRV_VH) {
                                    int selectedCategory = categoryRVAdapter.selectedCategory;
                                    if (selectedCategory != -1) {
                                        try {
                                            Notes noteObj = categoryList.get(selectedCategory - 1).getNotesList().get(viewHolder.getAdapterPosition());
                                            boolean isDeleted = new DBHelper(context).deleteRecoverNote(noteObj.getNoteId(), "Y");
                                            if (isDeleted) {
                                                AppUtils.showToastMessage(context, "Note Deleted successfully", true);
                                                categoryList.get(selectedCategory - 1).getNotesList().remove(noteObj);
                                                categoryRVAdapter.setItems(categoryList);
                                                notesRVAdapter.setNotesList(categoryList.get(selectedCategory - 1).getNotesList());
                                                notesRVAdapter.notifyDataSetChanged();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
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
                                if (viewHolder instanceof NotesRVAdapter.NotesRV_VH) {
                                    int selectedCategory = categoryRVAdapter.selectedCategory;
                                    if (selectedCategory != -1) {
                                        try {
                                            movableNotesObj = categoryList.get(selectedCategory - 1).getNotesList().get(viewHolder.getAdapterPosition());
                                            ChooseCategoryDialogFrag dialogFrag = new ChooseCategoryDialogFrag(categoryList, categoryRVAdapter, listener);
                                            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
                                            dialogFrag.ignoreCategoryPos = categoryRVAdapter.selectedCategory - 1;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
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
                PlusButtonBSFragment plusButtonBSFragment = new PlusButtonBSFragment(getSupportFragmentManager(), categoryRVAdapter, categoryList);
                plusButtonBSFragment.show(getSupportFragmentManager(), plusButtonBSFragment.getTag());
            }
        });

        // If there is no data in the notes update the UI accordingly

        // Bottom Navigation View
        final Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.home_menu).setIcon(R.drawable.home_selected);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.home_menu) {
                            item.setIcon(R.drawable.home_selected);
                            categoryList = new DBHelper(context).fetchAllCategories();
                            categoryRVAdapter.setItems(categoryList);
                            //If there is any change in the delete/pinned data in the notes should be updated
                            try {
                                if (categoryRVAdapter.selectedCategory != -1) {
                                    notesRVAdapter.setNotesList(categoryList.get(categoryRVAdapter.selectedCategory - 1).getNotesList());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                            showTrashMenuItems();
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
        try {
            boolean isEmptyPinnedLayout = true;
            pinnedNotesList = new DBHelper(this).fetchPinnedNotes();
            dayWiseList = AppUtils.getPinnedNotesByDay(pinnedNotesList, true, false);
            if (!dayWiseList.isEmpty()) {
                isEmptyPinnedLayout = false;
                todayRV.setVisibility(View.VISIBLE);
                todayTxt.setVisibility(View.VISIBLE);
                if (todaysAdapter == null) {
                    todaysAdapter = new PinnedNotesRVAdapter(dayWiseList, this);
                    todayRV.setAdapter(todaysAdapter);
                }
                todaysAdapter.setPinnedNotesList(dayWiseList);
            } else {
                todayRV.setVisibility(View.GONE);
                todayTxt.setVisibility(View.GONE);
            }

            dayWiseList = AppUtils.getPinnedNotesByDay(pinnedNotesList, false, true);
            if (!dayWiseList.isEmpty()) {
                isEmptyPinnedLayout = false;
                yesterdayRV.setVisibility(View.VISIBLE);
                yesterdayTxt.setVisibility(View.VISIBLE);
                if (yesterdaysAdapter == null) {
                    yesterdaysAdapter = new PinnedNotesRVAdapter(dayWiseList, this);
                    yesterdayRV.setAdapter(yesterdaysAdapter);
                }
                yesterdaysAdapter.setPinnedNotesList(dayWiseList);
            } else {
                yesterdayRV.setVisibility(View.GONE);
                yesterdayTxt.setVisibility(View.GONE);
            }

            dayWiseList = AppUtils.getPinnedNotesByDay(pinnedNotesList, false, false);
            if (!dayWiseList.isEmpty()) {
                isEmptyPinnedLayout = false;
                oldRV.setVisibility(View.VISIBLE);
                oldTxt.setVisibility(View.VISIBLE);
                if (oldAdapter == null) {
                    oldAdapter = new PinnedNotesRVAdapter(dayWiseList, this);
                    oldRV.setAdapter(oldAdapter);
                }
                oldAdapter.setPinnedNotesList(dayWiseList);
            } else {
                oldRV.setVisibility(View.GONE);
                oldTxt.setVisibility(View.GONE);
            }
            if (isEmptyPinnedLayout) {
                findViewById(R.id.default_pinned_layout).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.default_pinned_layout).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showTrashMenuItems() {
        List<Notes> deletedNotes = new DBHelper(this).fetchDeletedNotes();
        View emptyDeleteView = findViewById(R.id.default_trash);
        if (deletedNotes != null && !deletedNotes.isEmpty()) {
            emptyDeleteView.setVisibility(View.GONE);
        } else {
            emptyDeleteView.setVisibility(View.VISIBLE);
        }
        TrashNotesRVAdapter adapter = new TrashNotesRVAdapter(deletedNotes, categoryList, emptyDeleteView, this);
        RecyclerView deleteNotesRV = findViewById(R.id.delete_notes_rv);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        deleteNotesRV.setLayoutManager(layoutManager);
        deleteNotesRV.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                categoryList = new DBHelper(this).fetchAllCategories();
                categoryRVAdapter.setItems(categoryList);
                if (data != null && data.getExtras() != null && data.getExtras().get("category_id") != null) {
                    if (data.getExtras().get("fromPinnedNotes") != null) {
                        showPinnedNotes();
                    }
                    int selectedCategoryId = data.getExtras().getInt("category_id");
                    if (selectedCategoryId != -1) {
                        notesRVAdapter.setNotesList(categoryList.get(selectedCategoryId).getNotesList());
                        notesRv.smoothScrollToPosition(notesRv.getBottom());
                        categoryRVAdapter.selectedCategory = selectedCategoryId + 1;
                        categoryRV.smoothScrollToPosition(selectedCategoryId + 1);
                        categoryRVAdapter.notifyDataSetChanged();
                    } else {
                        selectedCategoryId = categoryRVAdapter.selectedCategory;
                        if (selectedCategoryId != -1) {
                            notesRVAdapter.setNotesList(categoryList.get(selectedCategoryId - 1).getNotesList());
                        }
                    }
                } else {
                    // Category created in the notes page but notes not saved
                    // Then refresh the data from the DB and change the focus to the last created category
                    categoryRVAdapter.selectedCategory = categoryList.size();
                    categoryRV.smoothScrollToPosition(categoryList.size());
                    categoryRVAdapter.notifyDataSetChanged();
                    notesRVAdapter.setNotesList(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCategorySelection(int selectedCategoryId) {
        try {
            if (movableNotesObj != null) {
                categoryList = new DBHelper(this).fetchAllCategories();
                boolean isUpdated = new DBHelper(this).moveNotes(movableNotesObj.getNoteId(), categoryList.get(selectedCategoryId).getCategoryId());
                if (isUpdated) {
                    categoryList = new DBHelper(this).fetchAllCategories();
                    categoryRVAdapter.setItems(categoryList);
                    categoryRVAdapter.selectedCategory = selectedCategoryId + 1;
                    categoryRV.smoothScrollToPosition(selectedCategoryId + 1);
                    notesRVAdapter.setNotesList(categoryList.get(selectedCategoryId).getNotesList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewCategoryCreation(List<Category> updatedCategory) {

    }
}
