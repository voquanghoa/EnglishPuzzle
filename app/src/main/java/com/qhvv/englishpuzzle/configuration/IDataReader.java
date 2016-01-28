package com.qhvv.englishpuzzle.configuration;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by voqua on 11/9/2015.
 */
public interface IDataReader {
    Bitmap readAsBitmap(Context context, String subPath) throws IOException;
    MediaPlayer createMediaPlayer(Context context, String subPath) throws IOException;
    boolean isFileExist(String filePath);
    File getFile(String path);
    Bitmap getKeyboardBackground(Context context, String category);
    Bitmap getMainBackground(Context context, String category);
    Bitmap getBackground(Context context, String category);
}
