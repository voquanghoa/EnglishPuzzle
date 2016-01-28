package com.qhvv.englishpuzzle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qhvv.englishpuzzle.control.CategoryControl;
import com.qhvv.englishpuzzle.model.Category;

import java.util.List;

/**
 * Created by Vo Quang Hoa on 20/09/2015.
 */
public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categories;
    private ICategorySelected iCategorySelected;

    public CategoryAdapter(Context context, List<Category> categories, ICategorySelected iCategorySelected){
        this.context = context;
        this.categories = categories;
        this.iCategorySelected = iCategorySelected;
    }

    public int getCount() {
        return categories.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryControl categoryControl;

        if(convertView == null){
            categoryControl = new CategoryControl(context);
        }else{
            categoryControl = (CategoryControl)convertView;
        }

        final Category category = categories.get(position);
        categoryControl.setImageBitmap(category.getIcon());
        categoryControl.setOnClickAction(new View.OnClickListener() {
            public void onClick(View v) {
                iCategorySelected.categorySelected(category);
            }
        });

        return categoryControl;
    }
}
