package com.qhvv.englishpuzzle.controller;

import com.qhvv.englishpuzzle.model.Category;
import com.qhvv.englishpuzzle.model.Word;
import com.qhvv.englishpuzzle.util.RandomComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by voqua on 11/22/2015.
 */
public class DataController {

    private static DataController instance;
    public static DataController getInstance(){
        if(instance==null){
            instance = new DataController();
        }
        return instance;
    }
    private ArrayList<Category> categories;
    public ArrayList<Category> getCategories(){
        return categories;
    }

    public void updateCategoryList(){
        this.categories = new ArrayList<Category>();
        categories.addAll(AssertDataController.getInstance().getCategories());
        categories.addAll(StorageController.getInstance().getCategories());
    }

    public Category getCategory(int index){
        return categories.get(index);
    }
    public Category getCategory(String categoryName){
        for(int i=0;i< categories.size();i++){
            Category category = categories.get(i);
            if(category.getName().endsWith(categoryName)){
                return category;
            }
        }
        return null;
    }

    public Category getRandomCategory(){
        Random random = new Random();
        return  categories.get(random.nextInt(categories.size()));
    }

    private List<Word> getAllWords(){
        List<Word> words = new ArrayList<Word>();

        for (Category category : categories) {
            words.addAll(category.getWords());
        }

        return words;
    }

    public Word[] getRandom4Words(){
        Object[] words = getAllWords().toArray();
        Arrays.sort(words, new RandomComparator());
        return Arrays.copyOfRange(words,0,4, Word[].class);
    }
}
