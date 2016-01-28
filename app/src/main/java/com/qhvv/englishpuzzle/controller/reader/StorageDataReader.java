package com.qhvv.englishpuzzle.controller.reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;

import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.configuration.IDataReader;
import com.qhvv.englishpuzzle.controller.StorageController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by voqua on 11/9/2015.
 */
public class StorageDataReader implements IDataReader,AppConstants {

    public boolean isFileExist(String filePath){
        return getFile(filePath).exists();
    }

    public File getFile(String filePath) {
        String fullPath = StorageController.getInstance().getBaseDir()+ "/" + PACKAGE_FOLDER+"/"+filePath;
        return new File(fullPath);
    }

    public Bitmap getMainBackground(Context context, String category) {
        try
        {
            return readAsBitmap(context, category + BACKGROUND_MAIN);
        }
        catch (IOException ex){
            return null;
        }
    }

    @Override
    public Bitmap getBackground(Context context, String category) {
        try
        {
            return readAsBitmap(context, category + BACKGROUND_CATEGORY);
        }
        catch (IOException ex){
            return null;
        }
    }

    public Bitmap getKeyboardBackground(Context context, String category) {
        try{
            return readAsBitmap(context,category + BACKGROUND_KEYBOARD );
        }catch (IOException ex){
            return null;
        }
    }

    public Bitmap readAsBitmap(Context context, String subPath) throws IOException {
        String bitmapPath = StorageController.getInstance().getBaseDir()+ "/" + String.format(AppConstants.STORAGE_BITMAP_FILE_PATH,subPath);
        InputStream ims = new FileInputStream(new File(bitmapPath));

        return  BitmapFactory.decodeStream(ims);
    }

    public MediaPlayer createMediaPlayer(Context context, String subPath) throws IOException {
        String audioPath = StorageController.getInstance().getBaseDir()+"/"+ String.format(AppConstants.STORAGE_AUDIO_FILE_PATH, subPath);

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(audioPath)));

        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.setLooping(false);

        return mediaPlayer;
    }

    private static StorageDataReader instance;
    public static StorageDataReader getInstance(){
        if(instance==null){
            instance = new StorageDataReader();
        }
        return instance;
    }
}
