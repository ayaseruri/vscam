package com.x.vscam.upload;

import java.io.File;
import java.io.IOException;

import org.androidannotations.annotations.EActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.ImgUploadUtils;
import com.x.vscam.settings.UploadAvatarResponseBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import ykooze.ayaseruri.codesslib.io.FileUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity
public class ImgUploadActivity extends BaseActivity {

    private static final short OPEN_GALLERY_REQUEST = 100;

    private View mRootView;
    private ContentLoadingProgressBar mProgressBar;
    private TextView mFliterName;
    private TextInputLayout mDescrption;
    private UploadResponseBean mUploadResponseBean;
    private SimpleDraweeView mImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImgUploadUtils.openGalleryForResult(this, OPEN_GALLERY_REQUEST);
    }

    private synchronized void initView(){
        if(null == mRootView){
            mRootView = LayoutInflater.from(this).inflate(R.layout.activity_img_upload, null);
            mProgressBar = (ContentLoadingProgressBar) mRootView.findViewById(R.id.progress_bar);
            mFliterName = (TextView) mRootView.findViewById(R.id.fliter_name);

            mDescrption = (TextInputLayout) mRootView.findViewById(R.id.description);
            mDescrption.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(R.integer.action_sub_img == actionId && KeyEvent.ACTION_UP == event.getAction()){
                        if(null == mUploadResponseBean){
                            Snackbar.make(mRootView, View.VISIBLE == mProgressBar.getVisibility() ? "请等待图片上传完成"
                                    : "请添加一张图片", Snackbar.LENGTH_LONG).show();
                            return false;
                        }else {
                            subImg(mUploadResponseBean);
                        }
                    }
                    return true;
                }
            });

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

    private void subImg(UploadResponseBean uploadResponseBean){
        final SweetAlertDialog progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("正在发布图片");

        SubImgBean subImgBean = new SubImgBean();
        ApiIml.getInstance(this).subImg(subImgBean).compose(RxUtils.<UploadAvatarResponseBean>applySchedulers())
                .subscribe(new Observer<UploadAvatarResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressDialog.show();
                    }

                    @Override
                    public void onNext(UploadAvatarResponseBean uploadAvatarResponseBean) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Snackbar.make(mRootView, "图片发布失败", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        progressDialog.setTitleText("发布成功");
                        progressDialog.setConfirmText("确定");
                        progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        });
                    }
                });
    }

    private void uploadImg(File imgFile) throws IOException {
        RequestBody imgRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
        ApiIml.getInstance(this).uploadImg(imgRequestBody).compose(RxUtils.<UploadResponseBean>applySchedulers())
                .compose(this.<UploadResponseBean>bindToLifecycle())
                .flatMap(new Function<UploadResponseBean, ObservableSource<UploadResponseBean>>() {
                    @Override
                    public ObservableSource<UploadResponseBean> apply(final UploadResponseBean uploadResponseBean) throws Exception {
                            return TextUtils.isEmpty(uploadResponseBean.getError()) ?
                                    Observable.create(new ObservableOnSubscribe<UploadResponseBean>() {
                                @Override
                                public void subscribe(ObservableEmitter<UploadResponseBean> e) throws Exception {
                                    e.onNext(uploadResponseBean);
                                    e.onComplete();
                                }
                            }).compose(RxUtils.<UploadResponseBean>applySchedulers())
                                    : Observable.<UploadResponseBean>error(new ImgFormatErrorException(uploadResponseBean.getError()));
                    }
                }).subscribe(new Observer<UploadResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mUploadResponseBean = null;
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UploadResponseBean uploadResponseBean) {
                        mUploadResponseBean = uploadResponseBean;
                        mImg.setImageURI(String.format(Constans.OG_IMG_URL_PRE, mUploadResponseBean.getOrigin()));
                        mDescrption.requestFocus();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                        Snackbar.make(mRootView, e instanceof ImgFormatErrorException ? e.getMessage()
                                : "图片上传出错", Snackbar
                                .LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }
}
