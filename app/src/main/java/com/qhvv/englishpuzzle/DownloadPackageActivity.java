package com.qhvv.englishpuzzle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qhvv.englishpuzzle.adapter.DownloadAdapter;
import com.qhvv.englishpuzzle.configuration.AppConstants;
import com.qhvv.englishpuzzle.configuration.IRequestHande;
import com.qhvv.englishpuzzle.configuration.IWebService;
import com.qhvv.englishpuzzle.controller.HttpDownloadController;
import com.qhvv.englishpuzzle.controller.WebController;
import com.qhvv.englishpuzzle.controller.ZipController;
import com.qhvv.englishpuzzle.model.LocalPackageInfo;
import com.qhvv.englishpuzzle.model.webmodel.PackageInfo;
import com.qhvv.englishpuzzle.useinterface.BaseActivity;
import com.qhvv.englishpuzzle.util.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.qhvv.englishpuzzle.controller.StorageController.getInstance;

/**
 * Created by voqua on 10/31/2015.
 */
public class DownloadPackageActivity extends BaseActivity implements DownloadAdapter.IDownloadCommand, HttpDownloadController.IDownload {

    private ListView categoryListView;
    private DownloadAdapter downloadAdapter;
    private ProgressDialog progressDialog;
    private LocalPackageInfo packageInfoDownload;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.download_screen_layout);
        categoryListView = (ListView)findViewById(R.id.categories_to_download);
        downloadAdapter=new DownloadAdapter(this);
        downloadAdapter.setFeedback(this);

        if(!getInstance().testFileSystem()){
            Utils.Log("Init file system fail. Exit");
            finish();
            showMessage(getString(R.string.local_file_warning));
            return;
        }

        loadCategoriesList();
        createLoadingDialog();
    }

    private void showCategories(String jsonData){
        Type listType = new TypeToken<ArrayList<PackageInfo>>(){}.getType();
        ArrayList<PackageInfo> webCategories = new Gson().fromJson(jsonData, listType);
        ArrayList<LocalPackageInfo> localCategories = new ArrayList<>();
        for(int i=0;i<webCategories.size();i++){
            localCategories.add(new LocalPackageInfo(webCategories.get(i)));
        }
        downloadAdapter.setPackageInfoList(localCategories);
        categoryListView.setAdapter(downloadAdapter);

        getInstance().downloadCategoriesInfor(localCategories, new Runnable() {
            public void run() {
                DownloadPackageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        downloadAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void loadCategoriesList(){
        showLoadingDialog();
        WebController.getInstance().DownloadContent(this, IWebService.PACKAGE_INFO_LIST, new IRequestHande<String>() {
            public void onError(String reason) {
                Toast.makeText(DownloadPackageActivity.this, getString(R.string.download_fail_message), Toast.LENGTH_LONG).show();
                closeLoadingDialog();
            }

            public void onSuccess(String data) {
                showMessage(getString(R.string.refresh_list_finish));
                showCategories(data);
                closeLoadingDialog();
            }
        });
    }

    private  void createLoadingDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.download_dialog_title_common));
        progressDialog.setTitle(getString(R.string.download_dialog_text));
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(this);
        progressDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                stopDownload();
            }
        });
    }

    public void finish() {
        super.finish();
        try{
            getInstance().updateCategories();
        }catch (IOException ex){
            Utils.Log(ex);
        }
    }

    public void onCancel(DialogInterface dialog) {
        progressDialog.hide();
    }

    public void onLocalPackageInfo(LocalPackageInfo pack) {
        packageInfoDownload = pack;
        progressDialog.setMessage(String.format(getString(R.string.download_dialog_title), pack.getDisplay()));
        progressDialog.show();
        getInstance().createFolder(AppConstants.PACKAGE_FOLDER);
        String path = getInstance().getFullPath(AppConstants.PACKAGE_FOLDER+"/"+pack.getFileName());
        HttpDownloadController.getInstance().startDownload(AppConstants.PACKAGE_FOLDER + "/" + pack.getFileName(), path, this);
    }

    public void onDownloadDone() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                getInstance().UnzipFile(packageInfoDownload, new ZipController.IZipHander() {
                    public void onSuccess() {
                        DownloadPackageActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.hide();
                                downloadAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    public void onFail(String reason) {
                        DownloadPackageActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                showMessage(getString(R.string.download_finish));
                                progressDialog.setProgress(0);
                                progressDialog.hide();
                            }
                        });
                    }
                });
            }
        });
    }

    public void onDownloadFail(final String message) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                showMessage(message);
                progressDialog.setProgress(0);
                progressDialog.hide();
            }
        });
    }

    public void onDownloadProgress(final int done, final int total) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                int mb =1024*1024;
                progressDialog.setMessage(String.format(getString(R.string.download_dialog_progress_message),
                        packageInfoDownload.getDisplay(), 1.0 * done / mb, 1.0 * total / mb));
                progressDialog.setProgress(done * 100 / total);
            }
        });
    }

    public void onRefreshClicked(View view){
        loadCategoriesList();
    }

    private void stopDownload(){
        HttpDownloadController.getInstance().stopDownload();
    }
}
