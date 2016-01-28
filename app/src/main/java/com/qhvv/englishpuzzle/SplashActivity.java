package com.qhvv.englishpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.qhvv.englishpuzzle.useinterface.BaseActivity;

/**
 * Created by Vo Quang Hoa on 03/10/2015.
 */
public class SplashActivity extends BaseActivity implements Runnable {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        new Handler().postDelayed(this, 3000);
    }

    public void run() {
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}
