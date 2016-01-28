package com.qhvv.englishpuzzle.controller;

import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by voqua on 11/1/2015.
 */
public class ZipController {
    public interface IZipHander{
        void onSuccess();
        void onFail(String reason);
    }

    private static  ZipController instance;
    public static synchronized ZipController getInstance(){
        if(instance==null){
            instance = new ZipController();
        }
        return instance;
    }

    private ZipController(){

    }

    public void startUnZipFile(final File zipFile, final File targetDirectory, final String desDoneFile, final IZipHander finishFeedback){
        new Thread(new Runnable() {
            public void run() {
                try {
                    UnzipFile(zipFile, targetDirectory, finishFeedback);
                    zipFile.delete();
                    File file = new File(desDoneFile, AppConstants.DONE_FILE_NAME);
                    file.createNewFile();

                } catch (Exception e) {
                    Utils.Log(e);
                    finishFeedback.onFail(e.getMessage());
                }
            }
        }).start();
    }

    private void UnzipFile(File zipFile, File targetDirectory, IZipHander finishFeedback) throws FileNotFoundException,Exception{
        if(!targetDirectory.exists()){
            targetDirectory.mkdir();
        }

        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null){
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs()){
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }

                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try{
                    while ((count = zis.read(buffer)) != -1){
                        fout.write(buffer, 0, count);
                    }
                }finally{
                    fout.close();
                }
            }
        }
        finally{
            zis.close();
            finishFeedback.onSuccess();
        }
    }
}
