package com.qhvv.englishpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.qhvv.englishpuzzle.adapter.CategoryAdapter;
import com.qhvv.englishpuzzle.adapter.ICategorySelected;
import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.controller.DataController;
import com.qhvv.englishpuzzle.model.Category;
import com.qhvv.englishpuzzle.useinterface.BaseActivity;

public class CategorySelectionActivity extends BaseActivity implements  AdapterView.OnItemClickListener, Runnable, ICategorySelected {
    private GridView gridView;
    private CategoryAdapter categoryAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_selection_layout);

        gridView = (GridView)findViewById(R.id.category_listview);
        gridView.setOnItemClickListener(this);
        DataController.getInstance().updateCategoryList();
        updateCategoryList();
    }

    protected void onResume() {
        super.onResume();
        updateCategoryList();
    }

    private void updateCategoryList(){
        categoryAdapter = new CategoryAdapter(this, DataController.getInstance().getCategories(), this);
        gridView.setAdapter(categoryAdapter);
    }



    public void btDownloadClicked(View view){
        startActivity(new Intent(this, DownloadPackageActivity.class));
    }

    public void run() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                DataController.getInstance().updateCategoryList();
                updateCategoryList();
            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        categorySelected(DataController.getInstance().getCategory(position));
    }

    public void categorySelected(Category category) {
        Intent intent = new Intent(this,MainPuzzleActivity.class);
        Bundle bundle = new Bundle();

        String messageName = AppConstants.CATEGORY_MESSAGE;
        bundle.putString(messageName,category.getName());

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
