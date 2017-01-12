package com.x.vscam.upload;

import java.io.File;
import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.StartUtils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import ykooze.ayaseruri.codesslib.io.FileUtils;
import ykooze.ayaseruri.codesslib.net.progress.HttpProgressUtils;
import ykooze.ayaseruri.codesslib.net.progress.ProgressListener;
import ykooze.ayaseruri.codesslib.others.Utils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity
public class ImgUploadActivity extends BaseActivity {

    private static final short OPEN_GALLERY_REQUEST = 100;

    private View mRootView;
    private ContentLoadingProgressBar mProgressBar;
    private TextView mFliterName;
    private SimpleDraweeView mImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxUtils.<Boolean>applySchedulers())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean ok) throws Exception {
                        if(ok){
                            StartUtils.startGalleryForResult(ImgUploadActivity.this, OPEN_GALLERY_REQUEST);
                        }else {
                            showPermissionDialog();
                        }
                    }
                });
    }

    private synchronized void initView(){
        if(null == mRootView){
            mRootView = LayoutInflater.from(this).inflate(R.layout.activity_img_upload, null);
            mProgressBar = (ContentLoadingProgressBar) mRootView.findViewById(R.id.progress_bar);
            mFliterName = (TextView) mRootView.findViewById(R.id.fliter_name);
            mImg = (SimpleDraweeView) mRootView.findViewById(R.id.img);
            setContentView(mRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(OPEN_GALLERY_REQUEST == requestCode){
            if(Activity.RESULT_OK == resultCode){
                try {
                    String realImgPath = FileUtils.getUriPath(this, data.getData());
                    if(TextUtils.isEmpty(realImgPath)){
                        finish();
                    }else {
                        initView();
                        mImg.setImageURI(data.getData());
                        mProgressBar.setMax(100);
                        uploadImg(new File(realImgPath));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    finish();
                }
            }else if(Activity.RESULT_CANCELED == resultCode){
                finish();
            }
        }
    }


    private void uploadImg(File imgFile) throws IOException {
        RequestBody imgRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
        ApiIml.getInstance(this).uploadImg(
                HttpProgressUtils.addProgressRequestListener(imgRequestBody, new ProgressListener() {
                    @Override
                    public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                        setProgressBarProgress((int) ((bytesRead * 1.0f/ contentLength) * 100));
                    }
                })).compose(RxUtils.<UploadResponseBean>applySchedulers())
                .compose(this.<UploadResponseBean>bindToLifecycle())
                .subscribe(new Observer<UploadResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UploadResponseBean uploadResponseBean) {
                        mImg.setImageURI(String.format(Constans.OG_IMG_URL_PRE, uploadResponseBean.getOrigin()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    @UiThread
    void setProgressBarProgress(int progress){
        Logger.d("progress:  " + progress);
        mProgressBar.setProgress(progress);
    }

    private void showPermissionDialog(){
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("需要权限");
        dialog.setContentText("需要\"存储\"权限来打开您的照片");
        dialog.showCancelButton(true);
        dialog.setCancelText("拒绝");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        dialog.setConfirmText("设置");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Utils.startAppSettings(ImgUploadActivity.this);
            }
        });
        dialog.show();
    }
}
