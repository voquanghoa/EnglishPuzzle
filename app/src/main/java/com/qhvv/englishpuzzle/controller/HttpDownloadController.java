package com.qhvv.englishpuzzle.controller;

import com.qhvv.englishpuzzle.configuration.IWebService;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by voqua on 10/31/2015.
 */
public class HttpDownloadController implements IWebService{
    public interface IDownload{
        void onDownloadDone();
        void onDownloadFail(String message);
        void onDownloadProgress(int done, int total);
    }

    private boolean isDownloading;
    private boolean isStopped;

    private static HttpDownloadController instance;

    public synchronized static HttpDownloadController getInstance(){
        if(instance==null){
            instance = new HttpDownloadController();
        }
        return instance;
    }

    private HttpDownloadController(){}

    public void startDownload(final String url, final String savePath,final IDownload downloadHandler){
        new Thread(new Runnable() {
            public void run() {
                downloadFile(url,savePath,downloadHandler);
            }
        }).start();
    }

    public synchronized void stopDownload(){
        if(isDownloading){
            isStopped = true;
        }
    }

    private void downloadFile(String downloadUrl, String savePath,IDownload downloadHandler) {
        isDownloading = true;
        isStopped = false;
        Utils.Log("Download "+downloadUrl);
        Utils.Log("Save to "+savePath);
        try {
            URL url = new URL(BASE_URL + downloadUrl);
            File file = new File(savePath);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if(isStopped){
                isStopped = false;
                downloadHandler.onDownloadFail("Download cancelled !");
                return;
            }

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                int totalSize = httpConn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    downloadHandler.onDownloadProgress(downloadedSize, totalSize);

                    if(isStopped){
                        isStopped = false;
                        downloadHandler.onDownloadFail("Download cancelled !");
                        inputStream.close();
                        fos.close();
                        new File(savePath).delete();
                        return;
                    }
                }

                inputStream.close();
                fos.close();
                downloadHandler.onDownloadDone();
            }else{
                downloadHandler.onDownloadFail("Download error. Can not download this file.");
                Utils.Log(new Exception("Can not download file " + downloadUrl));
            }
        } catch (Exception e) {
            File file = new File(savePath);
            if(file.exists()){
                file.delete();
            }
            downloadHandler.onDownloadFail(e.getMessage());
            e.printStackTrace();
        } finally {

        }
    }
}
