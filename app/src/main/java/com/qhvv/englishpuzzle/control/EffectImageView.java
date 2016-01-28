package com.qhvv.englishpuzzle.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.qhvv.englishpuzzle.util.Utils;

/**
 * Created by Vo Quang Hoa on 03/10/2015.
 */
public class EffectImageView extends ImageView {

    private int effectColor = 0x77eeddff;
    private OnClickListener onClickAction;
    private String textOverlay;
    private int textOverlayWidth;
    private int textOverlayHeight;
    private Paint paint;
    private static Typeface tf ;

    public EffectImageView(Context context) {
        super(context);
        initView();
    }

    public EffectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EffectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView(){
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        EffectImageView.this.setColorFilter(effectColor, PorterDuff.Mode.SRC_ATOP);
                        EffectImageView.this.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        clickFeedback();
                        determineClickEvent(event);
                    case MotionEvent.ACTION_CANCEL: {
                        EffectImageView.this.clearColorFilter();
                        EffectImageView.this.invalidate();
                        break;
                    }
                }
                return true;
            }
        });


        if(tf == null){
            tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/RobotoCondensed-Regular.ttf");
        }

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(tf);
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scale * 20);
    }

    private void determineClickEvent(MotionEvent event)
    {
        boolean isValidX = event.getX()>=0 && event.getX()<=this.getWidth();
        boolean isValidY = event.getY()>=0 && event.getY()<=this.getHeight();

        if(isValidX && isValidY)
        {
            try
            {
                this.performClick();
            }
            catch (Exception ex){
                Utils.Log(ex);
            }
        }
    }
    private void clickFeedback(){
        OnClickListener action = onClickAction;

        if(action != null){
            action.onClick(this);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(textOverlay!=null && textOverlay!=""){
            canvas.drawText(textOverlay,getWidth()/2,getHeight()/2+textOverlayHeight,paint);
        }
    }

    public void setTextOverlay(String textOverlay){
        if(this.textOverlay != textOverlay){
            this.textOverlay = textOverlay;

            if(textOverlay!=null && textOverlay!=""){
                Rect bounds = new Rect();
                paint.getTextBounds(textOverlay,0,textOverlay.length(),bounds);

                textOverlayHeight = bounds.height()/2;
                textOverlayWidth = bounds.width()/2;
            }
            invalidate();
        }
    }

    public void setOnClickAction(OnClickListener action){
        onClickAction = action;
    }
}
