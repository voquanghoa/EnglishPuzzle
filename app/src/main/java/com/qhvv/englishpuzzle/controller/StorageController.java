package com.qhvv.englishpuzzle.controller;

import android.graphics.BitmapFactory;

import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.configuration.IWebService;
import com.qhvv.englishpuzzle.controller.reader.StorageDataReader;
import com.qhvv.englishpuzzle.model.Category;
import com.qhvv.englishpuzzle.model.LocalPackageInfo;
import com.qhvv.englishpuzzle.model.Word;
import com.qhvv.englishpuzzle.util.FileHelper;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by voqua on 10/10/2015.
 */
public class StorageController {
    private static StorageController instance;
    private StorageDataReader dataReader;
    private List<Category> categories = new ArrayList<>();

    public static synchronized StorageController getInstance(){
        if(instance==null){
            instance=new StorageController();
        }
        return instance;
    }

    private StorageController(){
        dataReader = StorageDataReader.getInstance();
    }

    private String baseDir;

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return baseDir ;
    }

    public String getFullPath(String path){
        return baseDir+"/"+path;
    }

    private File getFile(String path){
        return new File(getFullPath(path));
    }

    public boolean isFileExist(String path){
        return getFile(path).exists();
    }

    public  void createFolder(String path){
        File file = getFile(path);
        if(!file.exists()){
            file.mkdir();
        }
    }

    public void WriteFile(String path, String content) throws Exception {
        FileOutputStream fos = new FileOutputStream(getFile(path));
        fos.write(content.getBytes());
        fos.close();
    }

    public boolean testFileSystem(){
        String FILENAME = "hello_file.txt";
        String string = "hello world!";

        if(isFileExist(FILENAME)){
            return true;
        }else{
            try {
                WriteFile(FILENAME, string);
                Utils.Log("Everything ok");
                return true;
            } catch (Exception e) {
                Utils.Log(e);
                return false;
            }
        }
    }

    public void UnzipFile(LocalPackageInfo packageInfo,ZipController.IZipHander zipHander){
        String zipFile = AppConstants.PACKAGE_FOLDER +"/"+packageInfo.getFileName();
        String zipFolder = AppConstants.PACKAGE_FOLDER +"/"+ FileHelper.getFileNameWithoutExtension(packageInfo.getFileName());

        ZipController.getInstance().startUnZipFile(getFile(zipFile), getFile(AppConstants.PACKAGE_FOLDER), getFullPath(zipFolder), zipHander);
    }

    public boolean checkPackageDownloaded(LocalPackageInfo packageInfo){
        String zipFolder = AppConstants.PACKAGE_FOLDER
                + "/"+FileHelper.getFileNameWithoutExtension(packageInfo.getFileName())
                +"/"+ AppConstants.DONE_FILE_NAME;
        Utils.Log(zipFolder + isFileExist(zipFolder));
        return isFileExist(zipFolder);
    }

    public synchronized void downloadCategoriesInfor(List<LocalPackageInfo> localPackageInfos, final Runnable callback){
        String imgExtendsion = AppConstants.PNG_EXTENSION;
        for (final LocalPackageInfo pack: localPackageInfos  ) {

            String fileName = pack.getFileName().substring(0, pack.getFileName().length() - imgExtendsion.length())
                    +imgExtendsion;
            String url = IWebService.PACKAGE_INFO_DIR+fileName;
            Utils.Log(url);

            String localPath =AppConstants.PACKAGE_FOLDER + "/"+fileName;
            File file = getFile(AppConstants.PACKAGE_FOLDER);

            if(!file.exists()){
                file.mkdir();
            }

            final String localFullPath = getFullPath(localPath);

            if(isFileExist(localPath)){
                if(pack.getIcon()==null) {
                    pack.setIcon(BitmapFactory.decodeFile(localFullPath));
                }
            }else{
                HttpDownloadController.getInstance().startDownload(url, localFullPath, new HttpDownloadController.IDownload() {
                    public void onDownloadDone() {
                        pack.setIcon(BitmapFactory.decodeFile(localFullPath));
                        callback.run();
                    }
                    public void onDownloadFail(String message) {

                    }
                    public void onDownloadProgress(int done, int total) {

                    }
                });
            }
        }
    }

    public List<Category> getCategories(){
        return categories;
    }
    public void updateCategories() throws IOException {
        File file = getFile(AppConstants.PACKAGE_FOLDER);
        if (file.exists() && file.isDirectory()) {
            String[] folders = file.list(new FilenameFilter() {
                public boolean accept(File current, String name) {
                    File folder = new File(current, name);
                    if(folder.isDirectory()){

                        String done_file = name+"/"+AppConstants.DONE_FILE_NAME;
                        String cover_file = name+"/"+AppConstants.COVER_FILE_NAME+AppConstants.PNG_EXTENSION;

                        boolean doneFileExist = dataReader.isFileExist(done_file);
                        boolean coverFileExist = dataReader.isFileExist(cover_file);

                        return doneFileExist && coverFileExist;
                    }
                    return false;
                }
            });

            ArrayList<Category> categories = new ArrayList<Category>();
            for(String folder:folders){
                Category category = loadCategory(folder);
                if(category!=null){
                    categories.add(category);
                }
            }
            this.categories = categories;
        }else{
            this.categories.clear();
        }
        DataController.getInstance().updateCategoryList();
    }

    private Category loadCategory(final String categoryName){
        try{
            Category category = new Category();
            category.setName(categoryName);

            final String folderPath = categoryName;
            final String relativeCoverPath =  folderPath + "/" + AppConstants.COVER_FILE_NAME;

            category.setIcon(dataReader.readAsBitmap(null, relativeCoverPath));
            File folder  = dataReader.getFile(folderPath);

            String[] words = folder.list(new FilenameFilter() {
                public boolean accept(File current, String name) {
                    String mp3Path = FileHelper.getFileNameWithoutExtension(name) + AppConstants.AUDIO_EXETENSION;
                    if (name.endsWith(".png")) {
                        return dataReader.isFileExist(folderPath + "/" + mp3Path);
                    }
                    return false;
                }
            });
            category.setWords(new ArrayList<Word>());
            for(String word:words){
                Word objWord = new Word();
                objWord.setCategory(categoryName);
                objWord.setReader(dataReader);
                objWord.setWord(word.substring(0, word.length()- AppConstants.PNG_EXTENSION.length()));
                category.getWords().add(objWord);
            }

            return category;
        }catch (Exception ex){
            Utils.Log(ex);
            return null;
        }
    }
}
