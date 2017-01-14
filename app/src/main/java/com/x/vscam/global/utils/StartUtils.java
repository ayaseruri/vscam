package com.x.vscam.global.utils;

import com.x.vscam.global.Constans;
import com.x.vscam.imgdetail.ImgDetailActivity_;
import com.x.vscam.imgviewer.ImgViewerActivity_;
import com.x.vscam.login.LoginActivity_;
import com.x.vscam.main.ImgFlowBean;
import com.x.vscam.main.MainActivity_;
import com.x.vscam.upload.ImgUploadActivity_;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class StartUtils {
    public static void startMain(Context context, ActivityOptionsCompat options){
        ActivityCompat.startActivity(context, new Intent(context, MainActivity_.class), options.toBundle());
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

    public static void startGalleryForResult(Activity context, int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, requestCode);
    }
}
