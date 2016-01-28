package com.qhvv.englishpuzzle.control;

import android.content.Context;

/**
 * Created by voqua on 12/13/2015.
 */
public class CategoryControl extends AutoHeightControl {
    private final double SIZE_RATIO = 0.78;
    public CategoryControl(Context context) {
        super(context);
    }

    protected double getHeightRatio() {
        return SIZE_RATIO;
    }
}
