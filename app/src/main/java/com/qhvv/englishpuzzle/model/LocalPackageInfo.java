package com.qhvv.englishpuzzle.model;

import android.graphics.Bitmap;

import com.qhvv.englishpuzzle.model.webmodel.PackageInfo;

/**
 * Created by voqua on 11/1/2015.
 */
public class LocalPackageInfo extends PackageInfo {
    private Bitmap icon;

    public  LocalPackageInfo(PackageInfo packageInfo){
        this.setBackground(packageInfo.isBackground());
        this.setSize(packageInfo.getSize());
        this.setDisplay(packageInfo.getDisplay());
        this.setFileName(packageInfo.getFileName());
        this.setItemCount(packageInfo.getItemCount());
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
