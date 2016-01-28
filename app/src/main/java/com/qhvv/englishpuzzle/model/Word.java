package com.qhvv.englishpuzzle.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import com.qhvv.englishpuzzle.configuration.IDataReader;

import java.io.IOException;

/**
 * Created by voqua on 10/19/2015.
 */
public class Word {
    private String word;
    private String category;
    private IDataReader reader;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setReader(IDataReader reader) {
        this.reader = reader;
    }

    public MediaPlayer createMediaPlayer(Context context) throws IOException {
        return reader.createMediaPlayer(context, category+"/" + word);
    }

    public Bitmap getBitmap(Context context) throws IOException {
        return reader.readAsBitmap(context, category+"/" + word);
    }
}
