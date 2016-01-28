package com.qhvv.englishpuzzle.control;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by voqua on 10/18/2015.
 */
public class SoundControl {
    private MediaPlayer mediaPlayer;
    private static  SoundControl instance;
    private SoundControl(){
        mediaPlayer = new MediaPlayer();
    }

    public static SoundControl getInstance(){
        if(instance == null){
            instance=new SoundControl();
        }
        return instance;
    }

    public MediaPlayer createMediaPlayer(Context context, String path) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();

        AssetFileDescriptor descriptor = context.getAssets().openFd(path);
        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        descriptor.close();

        mediaPlayer.prepare();
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.setLooping(false);

        return mediaPlayer;
    }

    public void playSoundInAssert(Context context, String path){
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }

        try {
            mediaPlayer = createMediaPlayer(context, path);

            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
