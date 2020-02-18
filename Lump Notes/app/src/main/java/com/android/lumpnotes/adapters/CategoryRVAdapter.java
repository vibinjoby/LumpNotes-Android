package com.android.lumpnotes.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.fragment.AddCategoryDialogFrag;
import com.android.lumpnotes.listeners.EditChangeListener;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.models.Notes;
import com.android.lumpnotes.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;


public class CategoryRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements EditChangeListener {
    private List<Category> categoryList;
    private PopupMenu popupMenu;
    public FragmentManager fragmentManager;
    private Context context;
    public int selectedCategory = 1;
    private static int TYPE_ADD_CATEGORY = 1;
    private static int TYPE_ALL_CATEGORY = 2;
    public RecyclerView recyclerView;
    private CategoryRVAdapter adapterObj;
    private NotesRVAdapter notesAdapterobj;
    private EditChangeListener listener;

    public static class CategoryVH extends RecyclerView.ViewHolder {
        public ImageView categoryIcBorder;
        public TextView categoryName;
        public Button menuButton;
        public ImageView categoryIcon;

        public CategoryVH(View v) {
            super(v);
            categoryIcBorder = v.findViewById(R.id.category_ic_border);
            categoryName = v.findViewById(R.id.category_name);
            menuButton = v.findViewById(R.id.menuBtn);
            categoryIcon = v.findViewById(R.id.category_icon);
        }
    }

    public static class AddCategoryVH extends RecyclerView.ViewHolder {
        public Button addCategoryBtn;

        public AddCategoryVH(View v) {
            super(v);
            addCategoryBtn = v.findViewById(R.id.add_category_btn);
        }
    }

    public CategoryRVAdapter(List<Category> categoryList, FragmentManager fragmentManager,RecyclerView recyclerView) {
        super();
        adapterObj = this;
        listener = this;
        this.categoryList = categoryList;
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_ADD_CATEGORY) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.default_category, parent, false);
            AddCategoryVH vh = new AddCategoryVH(v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_rc_items, parent, false);
            CategoryVH vh = new CategoryVH(v);
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ADD_CATEGORY;
        } else {
            return TYPE_ALL_CATEGORY;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_ADD_CATEGORY) {
            ((AddCategoryVH) holder).addCategoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCategoryDialogFrag dialog = new AddCategoryDialogFrag(context,adapterObj,categoryList,false,null,null,null);
                    dialog.show(fragmentManager, dialog.getTag());
                }
            });

            ((AddCategoryVH) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCategoryDialogFrag dialog = new AddCategoryDialogFrag(context,adapterObj,categoryList,false,null,null,null);
                    dialog.show(fragmentManager,dialog.getTag());
                }
            });

        } else {
            ((CategoryVH) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedCategory = position;
                    if(notesAdapterobj!=null) {
                        notesAdapterobj.isSearchBarVisible = false;
                        notesAdapterobj.setNotesList(categoryList.get(position - 1).getNotesList());
                    }
                    notifyDataSetChanged();
                }
            });

            String uri = "com.android.lumpnotes:drawable/"+categoryList.get(position - 1).getCategoryIcon();

            int res = this.context.getResources().getIdentifier(uri, null, null);
            ((CategoryVH) holder).categoryIcon.setBackgroundResource(res);
            if (selectedCategory == position) {
                ((CategoryVH) holder).categoryIcBorder.setBackgroundResource(R.drawable.selected_category_view);
            } else {
                ((CategoryVH) holder).categoryIcBorder.setBackgroundResource(R.drawable.curved_view);
            }
            ((CategoryVH) holder).categoryName.setText(categoryList.get(position - 1).getCategoryName());
            ((CategoryVH) holder).menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu = new PopupMenu(v.getContext(), v);
                    createMenu(popupMenu.getMenu(), position - 1);
                    popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            popupMenu = null;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size() + 1;
    }

    private void createMenu(Menu menu, final int position) {
        menu.add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("edit clicked for " + categoryList.get(position).getCategoryName());
                AddCategoryDialogFrag dialog = new AddCategoryDialogFrag(context,adapterObj
                        ,categoryList,true,categoryList.get(position),null,listener);
                dialog.show(fragmentManager, dialog.getTag());
                return true;
            }
        });

        menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean isDeleteEligible = true;
                if(categoryList!=null && categoryList.get(position).getNotesList()!=null) {
                    for (Notes notes : categoryList.get(position).getNotesList()) {
                        if (notes.isDeleted().equalsIgnoreCase("N")) {
                            isDeleteEligible = false;
                        }
                    }
                }

                if(!isDeleteEligible) {
                    AppUtils.showToastMessage(context,"Category cannot be deleted when notes are present",false);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                    builder.setTitle("Are you sure you want to delete the category " + categoryList.get(position).getCategoryName() + " ?");
                    builder.setMessage("This will be deleted immediately.You can’t undo this action.")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    new DBHelper(context).deleteCategory(categoryList.get(position).getCategoryId());
                                    categoryList.remove(position);
                                    notifyChangeForDeletion(categoryList, position);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.create();
                    builder.show();
                }
                return true;
            }
        });
    }

    public void notifyChangeForInsert(List<Category> categoryList) {
        this.categoryList = categoryList;
        selectedCategory = categoryList.size();
        notesAdapterobj.setNotesList(categoryList.get(selectedCategory - 1).getNotesList());
        notesAdapterobj.notifyDataSetChanged();
        notifyDataSetChanged();
        recyclerView.scrollToPosition(categoryList.size());
    }

    public void notifyChangeForDeletion(List<Category> categoryList,int position) {
        this.categoryList = categoryList;
        AppUtils.showToastMessage(context,"Category deleted Successfully",true);
        selectedCategory = 1;
        notifyDataSetChanged();
        if(categoryList!=null && selectedCategory < categoryList.size()) {
            notesAdapterobj.setNotesList(categoryList.get(selectedCategory - 1).getNotesList());
        } else {
            notesAdapterobj.setNotesList(null);
        }
        notesAdapterobj.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(position);
    }

    public void notifyChangeForEdit(int position) {
        selectedCategory = position+1;
        notifyDataSetChanged();
        notesAdapterobj.setNotesList(categoryList.get(position).getNotesList());
        notesAdapterobj.notifyDataSetChanged();
        recyclerView.scrollToPosition(position + 1);
    }

    public void setItems(List<Category> categoryLst) {
        this.categoryList = categoryLst;
        notifyDataSetChanged();
    }

    public void setNotesAdapterobj(NotesRVAdapter notesAdapterobj) {
        this.notesAdapterobj = notesAdapterobj;
    }

    @Override
    public void onEditChange(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

}
