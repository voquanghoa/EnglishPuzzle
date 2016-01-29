package com.qhvv.englishpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.useinterface.BaseActivity;
import com.qhvv.englishpuzzle.util.Utils;

/**
 * Created by Vo Quang Hoa on 03/10/2015.
 */
public class MainMenuActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        initAds();
    }

    public void onLessonClicked(View view){
        startActivity(new Intent(this, CategorySelectionActivity.class));
    }

    public  void onExerciseClicked(View view){
        Intent intent = new Intent(this, MainPuzzleActivity.class);
        Bundle bundle = new Bundle();
        String messageName = AppConstants.CATEGORY_MESSAGE;
        bundle.putString(messageName,"");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onPuzzleClicked(View view){
        startActivity(new Intent(this, ExerciseSelectPictureActivity.class));
    }

    private void initAds() {
        loadFullAds();
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                finish();
            }
        });
    }

    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Utils.Log("DID NOT LOAD FULL SCREEN ADS");
            super.onBackPressed();
        }
    }
}
