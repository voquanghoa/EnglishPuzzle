package com.qhvv.englishpuzzle.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.qhvv.englishpuzzle.R;

/**
 * Created by voqua on 12/13/2015.
 */

public class AutoHeightControl extends EffectImageView {
    private float sizeRatio = 1;
    public AutoHeightControl(Context context) {
        super(context);
    }
    public AutoHeightControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRatio(attrs);
    }

    public AutoHeightControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRatio(attrs);
    }

    private void initRatio(AttributeSet attrs){
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.AutoHeightControl, 0, 0);
        sizeRatio = typedArray.getFloat(R.styleable.AutoHeightControl_height_ratio, sizeRatio);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(widthMeasure, (int)(getHeightRatio() * widthMeasure));
    }

    protected double getHeightRatio(){
        return sizeRatio;
    }
}