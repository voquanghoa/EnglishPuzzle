package com.qhvv.englishpuzzle;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qhvv.englishpuzzle.controller.DataController;
import com.qhvv.englishpuzzle.model.Word;
import com.qhvv.englishpuzzle.useinterface.BaseActivity;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.IOException;
import java.util.Random;

public class ExerciseSelectPictureActivity extends BaseActivity {
    private View[] selectionImages;
    private boolean isAudioReady = false;
    private MediaPlayer mediaPlayer;
    private int correctAnswer;
    private TextView messageTextView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_picture_layout);
        messageTextView = (TextView) findViewById(R.id.text_message);

        selectionImages = new View[]{
                findViewById(R.id.exercise_image_0),
                findViewById(R.id.exercise_image_1),
                findViewById(R.id.exercise_image_2),
                findViewById(R.id.exercise_image_3),
        };
        try {
            showImages();
        } catch (IOException e) {
            Utils.Log(e);
        }
    }

    private void showImages() throws IOException {
        Word[] words = DataController.getInstance().getRandom4Words();
        for(int i=0;i<words.length;i++){
            try {
                ((ImageView)selectionImages[i]).setImageBitmap(words[i].getBitmap(this));
            }catch (IOException ex){
                Utils.Log(ex);
            }
        }

        if(mediaPlayer!=null){
            mediaPlayer.release();
        }

        isAudioReady = false;
        correctAnswer = new Random().nextInt(4);
        mediaPlayer = words[correctAnswer].createMediaPlayer(this);
        isAudioReady = true;
        mediaPlayer.start();
    }

    public void onPlayButtonClicked(View view){
        if(isAudioReady){
            mediaPlayer.start();
        }
    }

    public void onSkipClicked(View view){
        try {
            showImages();
        } catch (IOException e) {
            Utils.Log(e);
        }
    }

    public void onAnswerClicked(View view){
        int answer = Integer.parseInt(view.getTag().toString());

        if(answer == correctAnswer){
            Toast.makeText(this, "You are correct !", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "NOT CORRECT !", Toast.LENGTH_SHORT).show();
        }

        messageTextView.setVisibility(View.VISIBLE);
    }

    public void run(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                messageTextView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
