package com.qhvv.englishpuzzle.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qhvv.englishpuzzle.R;
import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.controller.reader.AssertDataReader;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.IOException;


/**
 * Created by Vo Quang Hoa on 04/10/2015.
 */
public class KeyboardControl extends LinearLayout {
    private String word="Animal";
    private CharacterControl[] answerViews;

    private char[] userText = new char[30];
    private final int MAX_ANSWER_LENGTH = 10;

    private Runnable onWrongFeedback;
    private Runnable onCorrectFeedback;
    private Runnable onWinFeedback;

    private LinearLayout keyboardLayout;

    private OnClickListener keyboardListener = new OnClickListener() {
        public void onClick(View v) {
            char code = (char)v.getTag();
            int maxIndex = Math.min(answerViews.length,userText.length);
            for(int i=0;i<maxIndex;i++){
                if(userText[i]==0){
                    setContent(i,code);
                    break;
                }
            }
            SoundControl.getInstance().playSoundInAssert(getContext(),
                    AppConstants.KEYBOARD_AUDIOS_PATH +code+AppConstants.AUDIO_EXETENSION);
        }
    };
    public KeyboardControl(Context context) {
        super(context);
        initializing();

    }

    public KeyboardControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializing();
    }

    public KeyboardControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializing();
    }

    private void initializing(){
        LayoutInflater.from(this.getContext()).inflate(R.layout.keyboard_layout, this);

        initAnswerLayout();
        initKeyboard();
    }

    private void initAnswerLayout(){
        LinearLayout answerLayout =(LinearLayout) findViewById(R.id.layout_answer);
        OnClickListener clickListener = new OnClickListener() {
            public void onClick(View v) {
                /*EffectfulImageView button = (EffectfulImageView)v;
                int index = (int)button.getTag();
                setContent(index,(char)0);*/
            }
        };

        answerViews = new CharacterControl[MAX_ANSWER_LENGTH];
        answerLayout.setWeightSum(MAX_ANSWER_LENGTH);
        for(int i=0;i<answerViews.length;i++){
            CharacterControl answerItem = new CharacterControl(getContext(), answerLayout);
            answerItem.setTag(i);

            answerViews[i]=answerItem;
        }
    }

    private void initKeyboard(){
        String[] keyboardLayoutConfig = AppConstants.KEYBOARD_LAYOUT;

        keyboardLayout = (LinearLayout) findViewById(R.id.layout_keyboard);
        keyboardLayout.setWeightSum(keyboardLayoutConfig.length);
        LinearLayout currentLayout = null;

        int maxWeight = 0;
        for(int i=0;i<keyboardLayoutConfig.length;i++) {
            maxWeight = Math.max(maxWeight,keyboardLayoutConfig[i].length());
        }

        for(int i=0;i<keyboardLayoutConfig.length;i++) {
            currentLayout = createNewRow(maxWeight);
            keyboardLayout.addView(currentLayout);
            for (int j = 0; j < keyboardLayoutConfig[i].length(); j++) {
                currentLayout.addView(createKeyboardButton(keyboardLayoutConfig[i].charAt(j)));
            }
        }
    }

    private LinearLayout createNewRow(int weightSum){
        LinearLayout newRow = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.keyboard_row_layout, null);
        newRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        newRow.setGravity(Gravity.CENTER);
        newRow.setWeightSum(weightSum);
        return newRow;
    }

    public void clearInput(){
        int maxIndex = Math.min(answerViews.length,userText.length);
        for(int i=0;i<maxIndex;i++){
            userText[i]=0;
            answerViews[i].setText("");
        }
    }
    private void setContent(int index, char c){
        if(word.charAt(index)==c){
            userText[index]=c;
            answerViews[index].setText(c == 0 ? "" : "" + c);
            if(index == word.length()-1){
                onWinFeedback.run();
            }
            else{
                onCorrectFeedback.run();
            }
        }else{
            onWrongFeedback.run();
        }

    }

    private EffectImageView createKeyboardButton(char c){
        EffectImageView button = (EffectImageView)LayoutInflater.from(getContext()).inflate(R.layout.keyboard_button_layout,null);
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        int margin_top_bottom = (int)getResources().getDimension(R.dimen.keyboard_button_margin_top_bottom);
        int margin_left_right = (int)getResources().getDimension(R.dimen.keyboard_button_margin_right_left);
        layoutParams.setMargins(margin_left_right, margin_top_bottom, margin_left_right,  margin_top_bottom);
        button.setLayoutParams(layoutParams);

        try {
            button.setImageBitmap(AssertDataReader.getInstance().readAsBitmapFromAssert(getContext(), AppConstants.KEYBOARD_IMAGES_PATH + c + AppConstants.PNG_EXTENSION));
        } catch (IOException e) {
            Utils.Log(e);
        }
        button.setTag(c);

        button.setOnClickAction(keyboardListener);
        return button;
    }

    public void setWord(String word){
        this.word = word;

        for(int i=0;i<answerViews.length;i++){
            if(i<word.length()){
                answerViews[i].setVisibility(View.VISIBLE);
            }else{
                answerViews[i].setVisibility(View.GONE);
            }
        }
    }

    public void setBackground(Bitmap bitmap){
        if(bitmap != null){
            keyboardLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }

    public void setOnWrongFeedback(Runnable onWrongFeedback) {
        this.onWrongFeedback = onWrongFeedback;
    }

    public void setOnCorrectFeedback(Runnable onCorrectFeedback) {
        this.onCorrectFeedback = onCorrectFeedback;
    }

    public void setOnWinFeedback(Runnable onWinFeedback) {
        this.onWinFeedback = onWinFeedback;
    }
}
