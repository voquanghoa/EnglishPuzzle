package com.qhvv.englishpuzzle.application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.qhvv.englishpuzzle.controller.AssertDataController;
import com.qhvv.englishpuzzle.controller.StorageController;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.IOException;

/**
 * Created by voqua on 10/31/2015.
 */
public class EnglishPuzzleApplication extends Application {
    private static EnglishPuzzleApplication singleton;

    public EnglishPuzzleApplication getInstance(){
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;

        StorageController.getInstance().setBaseDir(getDataDir());

        try {
            AssertDataController.getInstance().loadAssert(this);
            StorageController.getInstance().updateCategories();
        } catch (IOException e) {
            Utils.Log(e);
        }
    }

    private String getDataDir(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            Utils.Log("Error Package name not found ");
            Utils.Log(e);
        }
        return "";
    }
}
