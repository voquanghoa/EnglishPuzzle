package com.qhvv.englishpuzzle;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.control.GifImageView;
import com.qhvv.englishpuzzle.control.KeyboardControl;
import com.qhvv.englishpuzzle.controller.DataController;
import com.qhvv.englishpuzzle.model.Category;
import com.qhvv.englishpuzzle.model.Word;
import com.qhvv.englishpuzzle.useinterface.BaseActivity;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Vo Quang Hoa on 04/10/2015.
 */
public class MainPuzzleActivity extends BaseActivity {
    private KeyboardControl keyboardControl;
    private String categoryName;
    private Category category;
    private ImageView webImageView;
    private MediaPlayer mediaPlayer;
    private Word currentWord;
    private boolean isAudioReady=false;
    private TextView titleText;

    private GifImageView imageCorrect;
    private GifImageView imageWrong;
    private GifImageView imageWin;
    private Animation animOvershoot ;
    private Animation animBonce;
    private boolean isLessonMode;
    private ImageView imageBg;
    private RelativeLayout mainContent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_puzzle_layout);

        initKeyboard();
        webImageView =(ImageView) findViewById(R.id.imageView);
        categoryName = getIntent().getExtras().getString(AppConstants.CATEGORY_MESSAGE);
        titleText =(TextView) findViewById(R.id.title_tv);
        imageCorrect = (GifImageView)findViewById(R.id.image_correct);
        imageWrong =(GifImageView)findViewById(R.id.image_wrong);
        imageWin =(GifImageView)findViewById(R.id.image_win);
        imageBg = (ImageView) findViewById(R.id.main_background);
        mainContent = (RelativeLayout) findViewById(R.id.main_screen);
        animOvershoot = AnimationUtils.loadAnimation(this, R.anim.overshoot);
        animBonce = AnimationUtils.loadAnimation(this, R.anim.bonce);

        isLessonMode = (categoryName.length()!=0);

        loadAssertWords();
    }

    private void initKeyboard(){
        keyboardControl =(KeyboardControl) findViewById(R.id.keyboard);

        keyboardControl.setOnCorrectFeedback(new Runnable() {
            public void run() {
                showAnimation(imageCorrect, null);
            }
        });

        keyboardControl.setOnWinFeedback(new Runnable() {
            public void run() {
                showAnimation(imageWin, new Runnable() {
                    public void run() {
                        loadAssertWords();
                        keyboardControl.clearInput();
                    }
                });
            }
        });

        keyboardControl.setOnWrongFeedback(new Runnable() {
            public void run() {
                showAnimation(imageWrong, null);
            }
        });
    }

    private void showAnimation(final GifImageView animation, final Runnable onFinish){
        animation.setVisibility(View.VISIBLE);
        animation.startAnimation();
        animation.startAnimation(animOvershoot);
        animation.postOnAnimation(new Runnable() {
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(AppConstants.ANIMATION_DURATION);
                            MainPuzzleActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    animation.setVisibility(View.GONE);
                                    if (onFinish != null) {
                                        onFinish.run();
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void loadAssertWords(){
        try{

            category = isLessonMode? DataController.getInstance().getCategory(this.categoryName):
                    DataController.getInstance().getRandomCategory();

            if(category!=null){
                Random random = new Random();
                currentWord = category.getWords().get(random.nextInt(category.getWords().size()));

                initAudio();
                showImage();
                initMisc();
            }else {
                Utils.Log("No word");
            }
        }catch (Exception ex){
            Utils.Log(ex);
        }
    }

    private void initMisc(){
        keyboardControl.setWord(currentWord.getWord());
        if(isLessonMode){
            titleText.setText(currentWord.getWord());
            titleText.startAnimation(animBonce);
        }else{
            titleText.setText("");
        }
    }

    private void initAudio() throws IOException {
        mediaPlayer = currentWord.createMediaPlayer(this);
        isAudioReady = true;
    }

    private void showImage() throws IOException {
        webImageView.setImageBitmap(currentWord.getBitmap(this));
        keyboardControl.setBackground(category.getKeyboardBackground(this));
        setMainBackground(category.getMainBackground(this));
        setTotalBackground(category.getBackground(this));
    }

    private void setTotalBackground(Bitmap bitmap){
        if(bitmap != null){
            mainContent.setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }

    private void setMainBackground(Bitmap bitmap){
        if(bitmap != null){
            imageBg.setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }

    public void onAudioClick(View view){
        if(isAudioReady){
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }else{
            showMessage("Audio was not ready yet.");
        }
    }
}
