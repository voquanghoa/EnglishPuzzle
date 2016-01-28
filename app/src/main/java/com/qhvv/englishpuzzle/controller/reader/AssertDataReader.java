package com.qhvv.englishpuzzle.controller.reader;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.configuration.IDataReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class AssertDataReader implements IDataReader,AppConstants {
    public Bitmap readAsBitmapFromAssert(Context context, String path) throws IOException {
        InputStream ims = context.getAssets().open(path);
        return  BitmapFactory.decodeStream(ims);
    }
    public Bitmap readAsBitmap(Context context, String subPath) throws IOException {
        String bitmapPath = String.format(AppConstants.ASSERT_BITMAP_FILE_PATH,subPath);
        return readAsBitmapFromAssert(context,bitmapPath);
    }

    public MediaPlayer createMediaPlayer(Context context, String subPath) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        String audioPath=String.format(ASSERT_AUDIO_FILE_PATH, subPath);

        AssetFileDescriptor descriptor = context.getAssets().openFd(audioPath);
        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        descriptor.close();

        mediaPlayer.prepare();
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.setLooping(false);

        return mediaPlayer;
    }

    public boolean isFileExist(String filePath) {
        return false;
    }

    public File getFile(String path) {
        return null;
    }

    public Bitmap getKeyboardBackground(Context context, String category) {
        try
        {
            return readAsBitmap(context, category + BACKGROUND_KEYBOARD);
        }
        catch (IOException ex){
            return null;
        }
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


    public String readAsString(Context context, String path) throws IOException {
        Scanner sc = new Scanner(context.getAssets().open(path));
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()){
            sb.append(sc.nextLine());
        }
        return sb.toString();
    }

    private static AssertDataReader instance;
    public static AssertDataReader getInstance(){
        if(instance==null){
            instance = new AssertDataReader();
        }
        return instance;
    }
}
