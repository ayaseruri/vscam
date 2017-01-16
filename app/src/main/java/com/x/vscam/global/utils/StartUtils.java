package com.x.vscam.global.utils;

import com.x.vscam.about.AboutActivity_;
import com.x.vscam.global.Constans;
import com.x.vscam.imgdetail.ImgDetailActivity_;
import com.x.vscam.imgviewer.ImgViewerActivity_;
import com.x.vscam.login.LoginActivity_;
import com.x.vscam.main.ImgFlowBean;
import com.x.vscam.main.MainActivity_;
import com.x.vscam.settings.SettingsActivity_;
import com.x.vscam.upload.ImgUploadActivity_;
import com.x.vscam.userdetail.UserDetailActivity_;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class StartUtils {
    public static void startMain(Context context){
        context.startActivity(new Intent(context, MainActivity_.class));
    }

    public static void startUpload(Context context){
        context.startActivity(new Intent(context, ImgUploadActivity_.class));
    }

    public static void startImgDetail(Context context, ImgFlowBean.GridsBean gridsBean, ActivityOptionsCompat options){
        Intent intent = new Intent(context, ImgDetailActivity_.class);
        intent.putExtra(Constans.KEY_GRID, (Parcelable) gridsBean);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    public static void startLogin(Context context, ActivityOptionsCompat options){
        ActivityCompat.startActivity(context, new Intent(context, LoginActivity_.class), options.toBundle());
    }

    public static void startImgViewer(Context context, String imgPath, ActivityOptionsCompat options){
        Intent intent = new Intent(context, ImgViewerActivity_.class);
        intent.putExtra(Constans.KEY_IMG_PATH, imgPath);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    public static void startAbout(Context context){
        context.startActivity(new Intent(context, AboutActivity_.class));
    }

    public static void startSettings(Context context){
        context.startActivity(new Intent(context, SettingsActivity_.class));
    }

    public static void startUserDetail(Context context, ActivityOptionsCompat options){
        Intent intent = new Intent(context, UserDetailActivity_.class);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
