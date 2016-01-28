package com.qhvv.englishpuzzle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qhvv.englishpuzzle.R;
import com.qhvv.englishpuzzle.control.EffectfulImageView;
import com.qhvv.englishpuzzle.controller.StorageController;
import com.qhvv.englishpuzzle.model.LocalPackageInfo;

import java.util.List;

import static com.qhvv.englishpuzzle.R.drawable.package_background;
import static com.qhvv.englishpuzzle.R.drawable.package_background_downloaded;

public class DownloadAdapter extends BaseAdapter {
    public void setFeedback(IDownloadCommand feedback) {
        this.feedback = feedback;
    }

    public void setPackageInfoList(List<LocalPackageInfo> packageInfoList) {
        this.packageInfoList = packageInfoList;
        this.notifyDataSetChanged();
    }

    public interface IDownloadCommand{
        void onLocalPackageInfo(LocalPackageInfo pack);
    }

    private IDownloadCommand feedback;
    private Context context;
    private List<LocalPackageInfo> packageInfoList;

    public DownloadAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return packageInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return packageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate (R.layout.category_line_layout,parent,false);
        }

        final LocalPackageInfo packageInfo = packageInfoList.get(position);

        ImageView imgIcon = (ImageView)convertView.findViewById(R.id.category_icon);
        TextView txtPackageName = (TextView) convertView.findViewById(R.id.txtCategoryName);
        TextView txtPackInfor = (TextView)convertView.findViewById(R.id.txtCategorySize);
        TextView txtPackInforStatus = (TextView)convertView.findViewById(R.id.txtStatus);
        EffectfulImageView btton = (EffectfulImageView)convertView.findViewById(R.id.btDownload);

        txtPackageName.setText(packageInfo.getDisplay());
        txtPackInfor.setText(String.format("Size %s, %s words",packageInfo.getSize(),""+packageInfo.getItemCount()));

        if(StorageController.getInstance().checkPackageDownloaded(packageInfo)){
            convertView.setBackgroundResource(package_background_downloaded);
            txtPackInforStatus.setText(R.string.already_download);
            btton.setVisibility(View.INVISIBLE);
        }else{
            convertView.setBackgroundResource(package_background);
            txtPackInforStatus.setText(R.string.ready_download);
            btton.setVisibility(View.VISIBLE);
        }

        if(packageInfo.getIcon()!=null){
            imgIcon.setImageBitmap(packageInfo.getIcon());
        }else{
            imgIcon.setImageResource(R.drawable.default_package);
        }

        btton.setOnClickAction(new View.OnClickListener(){
            public void onClick(View v) {feedback.onLocalPackageInfo(packageInfo);
            }
        });

        return convertView;
    }
}
