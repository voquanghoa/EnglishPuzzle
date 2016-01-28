package com.qhvv.englishpuzzle.useinterface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.qhvv.englishpuzzle.R;
import com.qhvv.englishpuzzle.configuration.IWebService;

/**
 * Created by Vo Quang Hoa on 20/09/2015.
 */
public class BaseActivity extends Activity implements IWebService, DialogInterface.OnCancelListener {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected  void showLoadingDialog(){
        if(progressDialog==null){
            progressDialog = ProgressDialog.show(this, "",getString(R.string.loading) , true);
            progressDialog.setOnCancelListener(this);
        }
        progressDialog.show();
    }

    protected  void closeLoadingDialog(){
        if(progressDialog!=null){
            progressDialog.hide();
        }
    }

    public void onCancel(DialogInterface dialog) {

    }

    protected void showMessage(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public void onHomeButtonClicked(View view){
        this.finish();
    }
}
