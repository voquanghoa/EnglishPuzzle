package com.qhvv.englishpuzzle.util;

/**
 * Created by voqua on 11/9/2015.
 */
public class FileHelper {
    public static String getFileNameWithoutExtension(String fileName){
        if(fileName.indexOf('.')>=0){
            int lastIndex = fileName.lastIndexOf('.');
            return fileName.substring(0,lastIndex);
        }
        return fileName;
    }
}
