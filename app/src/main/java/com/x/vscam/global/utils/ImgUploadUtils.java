package com.x.vscam.global.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.x.vscam.upload.ImgUploadActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import ykooze.ayaseruri.codesslib.others.*;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 2017/1/15.
 */

public class ImgUploadUtils {

    public static void openGalleryForResult(final Activity context, final int requestCode){
        new RxPermissions(context).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxUtils.<Boolean>applySchedulers())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean ok) throws Exception {
                        if(ok){
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            context.startActivityForResult(intent, requestCode);
                        }else {
                            showPermissionDialog(context);
                        }
                    }
                });
    }

    private static void showPermissionDialog(final Activity activity){
        SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("需要权限");
        dialog.setContentText("需要\"存储\"权限来打开您的照片");
        dialog.showCancelButton(true);
        dialog.setCancelText("拒绝");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                activity.finish();
            }
        });
        dialog.setConfirmText("设置");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                ykooze.ayaseruri.codesslib.others.Utils.startAppSettings(activity);
            }
        });
        dialog.show();
    }
}
