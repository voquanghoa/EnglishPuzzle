package com.qhvv.englishpuzzle.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qhvv.englishpuzzle.R;

/**
 * Created by voqua on 12/10/2015.
 */
public class CharacterControl extends RelativeLayout {
    private TextView textView;
    public CharacterControl(Context context, ViewGroup parent) {
        super(context);
        initView();
        parent.addView(this);
    }

    private void initView(){
        setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        LayoutInflater.from(getContext()).inflate(R.layout.anwser_character_layout, this, true);
        textView = (TextView) findViewById(R.id.text_view);
    }

    public void setText(String text){
        textView.setText(text);
    }
}
