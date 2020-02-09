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
import com.android.lumpnotes.fragment.AddCategoryDialogFrag;


public class CategoryRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] categoryArr;
    private PopupMenu popupMenu;
    private FragmentManager fragmentManager;
    private Context context;
    private int selectedCategory = -1;
    private static int TYPE_ADD_CATEGORY = 1;
    private static int TYPE_ALL_CATEGORY = 2;

    public static class CategoryVH extends RecyclerView.ViewHolder {
        public ImageView categoryIcBorder;
        public TextView categoryName;
        public Button menuButton;

        public CategoryVH(View v) {
            super(v);
            categoryIcBorder = v.findViewById(R.id.category_ic_border);
            categoryName = v.findViewById(R.id.category_name);
            menuButton = v.findViewById(R.id.menuBtn);
        }
    }

    public static class AddCategoryVH extends RecyclerView.ViewHolder {
        public Button addCategoryBtn;

        public AddCategoryVH(View v) {
            super(v);
            addCategoryBtn = v.findViewById(R.id.add_category_btn);
        }
    }

    public CategoryRVAdapter(String[] myDataset, FragmentManager fragmentManager) {
        this.categoryArr = myDataset;
        this.fragmentManager = fragmentManager;
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
                    AddCategoryDialogFrag dialog = new AddCategoryDialogFrag();
                    dialog.show(fragmentManager, dialog.getTag());
                }
            });

            ((AddCategoryVH) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCategoryDialogFrag dialog = new AddCategoryDialogFrag();
                    dialog.show(fragmentManager,dialog.getTag());
                }
            });

        } else {
            ((CategoryVH) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedCategory = position;
                    notifyDataSetChanged();
                    System.out.println("item clicked" + categoryArr[position - 1]);
                }
            });
            if (selectedCategory == position) {
                ((CategoryVH) holder).categoryIcBorder.setBackgroundResource(R.drawable.selected_category_view);
            } else {
                ((CategoryVH) holder).categoryIcBorder.setBackgroundResource(R.drawable.curved_view);
            }
            ((CategoryVH) holder).categoryName.setText(categoryArr[position - 1]);
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
        return categoryArr.length + 1;
    }

    private void createMenu(Menu menu, final int position) {
        menu.add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("edit clicked for " + categoryArr[position]);
                return true;
            }
        });

        menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("delete clicked for " + categoryArr[position]);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                builder.setTitle("Are you sure you want to delete the category " + categoryArr[position] + " ?");
                builder.setMessage("This will be deleted immediately.You can’t undo this action.")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
                return true;
            }
        });
    }
}
