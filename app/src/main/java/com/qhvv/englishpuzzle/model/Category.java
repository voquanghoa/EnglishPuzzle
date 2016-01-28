package com.qhvv.englishpuzzle.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.qhvv.englishpuzzle.configuration.IDataReader;

import java.util.ArrayList;

/**
 * Created by Vo Quang Hoa on 20/09/2015.
 */
public class Category {
    private String name;
    private String displayName;
    private Bitmap icon;
    private IDataReader reader;

    private ArrayList<Word> words;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public void setReader(IDataReader reader) {
        this.reader = reader;
    }

    public Bitmap getKeyboardBackground(Context context){
        return reader.getKeyboardBackground(context, this.getName());
    }

    public Bitmap getMainBackground(Context context){
        return reader.getMainBackground(context, this.getName());
    }

    public Bitmap getBackground(Context context){
        return reader.getBackground(context, this.getName());
    }
}
