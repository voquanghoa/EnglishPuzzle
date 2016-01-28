package com.qhvv.englishpuzzle.controller;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.controller.reader.AssertDataReader;
import com.qhvv.englishpuzzle.model.Category;
import com.qhvv.englishpuzzle.model.Word;
import com.qhvv.englishpuzzle.model.webmodel.WebCategory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by voqua on 10/17/2015.
 */
public class AssertDataController {

    private ArrayList<Category> categories;
    private static AssertDataController instance;
    private boolean isInitialized=false;
    private AssertDataReader dataReader = AssertDataReader.getInstance();
    public synchronized static AssertDataController getInstance(){
        if(instance==null){
            instance = new AssertDataController();
        }
        return instance;
    }

    private AssertDataController(){

    }

    public synchronized void loadAssert(Context context) throws IOException {

        if(!isInitialized){
            String file = dataReader.readAsString(context, AppConstants.DATA_CATEGORIES_JSON_PATH);
            Type listType = new TypeToken<ArrayList<WebCategory>>(){}.getType();
            ArrayList<WebCategory> webCategories = new Gson().fromJson(file, listType);

            this.categories = new ArrayList<>();

            for(int i=0;i<webCategories.size();i++){
                Category category = new Category();
                WebCategory webCategory = webCategories.get(i);

                category.setDisplayName(webCategory.getDisplay());
                category.setName(webCategory.getFolder());
                category.setWords(getAllWords(context,webCategory.getFolder()));

                String path = webCategory.getFolder()+"/"+AppConstants.COVER_FILE_NAME;

                categories.add(category);
                category.setIcon(AssertDataReader.getInstance().readAsBitmap(context, path));
                category.setReader(dataReader);

            }
            isInitialized=true;
        }
    }

    private ArrayList<Word> getAllWords(Context context, String category) throws IOException {

        String path = String.format(AppConstants.ASSERT_JSON_FILE, category);
        String file =  dataReader.readAsString(context, path);

        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> words =  new Gson().fromJson(file, listType);

        ArrayList<Word> returnWords = new ArrayList<>();
        for(int i=0;i<words.size();i++){
            Word word = new Word();
            word.setWord(words.get(i));
            word.setReader(dataReader);
            word.setCategory(category);
            returnWords.add(word);
        }
        return returnWords;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public byte[] readAsByteArray(Context context, String path) throws IOException{
        InputStream inputStream = context.getAssets().open(path);

        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();

        return bytes;
    }
}
