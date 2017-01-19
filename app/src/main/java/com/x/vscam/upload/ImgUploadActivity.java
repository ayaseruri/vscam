package com.x.vscam.upload;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.EActivity;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.ImgUploadUtils;
import com.x.vscam.global.utils.Utils;
import com.x.vscam.settings.UploadAvatarResponseBean;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
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
import ykooze.ayaseruri.codesslib.others.InputMethodUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity
public class ImgUploadActivity extends BaseActivity {

    private static final short OPEN_GALLERY_REQUEST = 100;
    private static final String NO_VSCO_JPEG = "只允许上传由VSCO调整的JEPG格式输出";

    private View mRootView;
    private ContentLoadingProgressBar mProgressBar;
    private TextView mFliterName;
    private TextInputLayout mDescrption;
    private UploadResponseBean mUploadResponseBean;
    private SimpleDraweeView mImg;
    private Toolbar mToolbar;

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
            mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);

            Utils.setDisplayHomeAsUp(this, mToolbar);
            mDescrption = (TextInputLayout) mRootView.findViewById(R.id.description);
            mDescrption.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(getResources().getInteger(R.integer.action_sub_img) == actionId){
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
                        File imgFile = new File(realImgPath);
                        initView();
                        if(checkImgInfo(imgFile)){
                            mImg.setImageURI(data.getData());
                            uploadImg(imgFile);
                        }else {
                            Utils.getSnackBar(this, NO_VSCO_JPEG)
                                    .setAction("重选", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImgUploadUtils.openGalleryForResult(ImgUploadActivity.this
                                                    , OPEN_GALLERY_REQUEST);
                                        }
                                    })
                                    .show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    finish();
                } catch (ImageProcessingException e) {
                    e.printStackTrace();
                }
            }else if(Activity.RESULT_CANCELED == resultCode){
                finish();
            }
        }
    }

    private boolean checkImgInfo(File imgFile) throws ImageProcessingException, IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
        String type = options.outMimeType;
        if("image/jpeg".equals(type)){
            Metadata metadata = ImageMetadataReader.readMetadata(imgFile);
            if(null != metadata){
                ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                if(null != directory){
                    String imgDescription = directory.getString(ExifSubIFDDirectory.TAG_IMAGE_DESCRIPTION);
                    if(!TextUtils.isEmpty(imgDescription) && imgDescription.contains("VSCO")){
                        mFliterName.setText(imgDescription.substring(imgDescription.lastIndexOf("with ") + "with ".length()
                                , imgDescription.lastIndexOf(" preset")));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void subImg(UploadResponseBean uploadResponseBean){
        final SweetAlertDialog progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setTitleText("正在发布图片");

        Map<String, Object> subMap = new HashMap<>();
        subMap.put("state", "1");
        subMap.put("gps", uploadResponseBean.getGps());
        subMap.put("exif", uploadResponseBean.getExif());
        subMap.put("preset", mFliterName.getText().toString());
        subMap.put("text", mDescrption.getEditText().getText().toString());
        subMap.put("pid", uploadResponseBean.getPid());

        ApiIml.getInstance(this).subImg(subMap).compose(RxUtils.<UploadAvatarResponseBean>applySchedulers())
                .flatMap(new Function<UploadAvatarResponseBean, ObservableSource<UploadAvatarResponseBean>>() {
                    @Override
                    public ObservableSource<UploadAvatarResponseBean> apply(UploadAvatarResponseBean uploadAvatarResponseBean)
                            throws Exception {
                        if(TextUtils.isEmpty(uploadAvatarResponseBean.getError())){
                            return Observable.just(uploadAvatarResponseBean);
                        }
                        return Observable.error(new UploadImgErrorException(uploadAvatarResponseBean.getError()));
                    }
                })
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
                        InputMethodUtils.hide(ImgUploadActivity.this, mRootView);
                        if(e instanceof UploadImgErrorException) {
                            Utils.getSnackBar(ImgUploadActivity.this, e.getMessage()).show();
                        }else {
                            Snackbar.make(mRootView, "图片发布失败", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        progressDialog.setTitleText("发布成功");
                        progressDialog.setConfirmText("确定");
                        progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
    }

    private void uploadImg(File imgFile) throws IOException {
        RequestBody imgRequestBody = RequestBody.create(MediaType.parse("image/jpg"), imgFile);
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
                        InputMethodUtils.hide(ImgUploadActivity.this, mRootView);
                        mUploadResponseBean = null;
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
