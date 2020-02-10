package com.android.lumpnotes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lumpnotes.R;
import com.android.lumpnotes.adapters.CategoryIconsAdapter;
import com.android.lumpnotes.adapters.CategoryRVAdapter;
import com.android.lumpnotes.dao.DBHelper;
import com.android.lumpnotes.models.Category;
import com.android.lumpnotes.utils.AppUtils;

import java.util.List;

public class AddCategoryDialogFrag extends DialogFragment implements TextWatcher {
    Button doneBtn;
    String selectedCategoryIcon;
    Context contextObj;
    CategoryRVAdapter adapter;
    List<Category> categoryList;

    public AddCategoryDialogFrag(Context contextObj, CategoryRVAdapter adapter,List<Category> categoryList) {
        this.adapter = adapter;
        this.contextObj = contextObj;
        this.categoryList = categoryList;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add_category,
                container, false);
        view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final EditText addCategoryText = view.findViewById(R.id.new_category);
        addCategoryText.addTextChangedListener(this);
        doneBtn = view.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidCategory = true;
                for(Category c:categoryList){
                    if(c.getCategoryName().trim().equalsIgnoreCase(addCategoryText.getText().toString())) {
                        isValidCategory = false;
                    }
                }
                if(!isValidCategory) {
                    AppUtils.showToastMessage(contextObj,"Category already exists",false);
                } else {
                    Category category = new Category();
                    category.setCategoryIcon(selectedCategoryIcon);
                    category.setCategoryName(addCategoryText.getText().toString());
                    categoryList.add(category);
                    dismiss();
                    adapter.notifyChangeForInsert(categoryList);
                    AppUtils.showToastMessage(contextObj,"New category created successfully",true);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            DBHelper dbHelper = new DBHelper(contextObj);
                            boolean isInserted = dbHelper.saveCategories(addCategoryText.getText().toString(),selectedCategoryIcon);
                            System.out.println("is inserted is "+isInserted);
                            System.out.println("selected icon is "+selectedCategoryIcon+" and the edit text is "+addCategoryText.getText().toString());
                        }
                    });
                }
            }
        });
        final GridView gridView = view.findViewById(R.id.icon_view);
        final CategoryIconsAdapter adapter = new CategoryIconsAdapter(getResources());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.icon_view).setBackgroundResource(R.drawable.selected_btn_view);
                adapter.selectedItemIndex = position;
                selectedCategoryIcon = "ctrgy_ic_"+(position+1);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s != null && s.toString().trim().length() > 0) {
            doneBtn.setEnabled(true);
        } else {
            doneBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
