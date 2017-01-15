package com.x.vscam.settings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.utils.ImgUploadUtils;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.StartUtils;
import com.x.vscam.global.utils.UserInfoUtils;
import com.x.vscam.upload.UploadResponseBean;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import ykooze.ayaseruri.codesslib.cache.CacheUtils;
import ykooze.ayaseruri.codesslib.io.FileUtils;
import ykooze.ayaseruri.codesslib.net.progress.HttpProgressUtils;
import ykooze.ayaseruri.codesslib.net.progress.ProgressListener;
import ykooze.ayaseruri.codesslib.rx.RxActivity;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends RxActivity {

    private static final short OPEN_GALLERY_REQUEST = 101;

    @ViewById(R.id.linear)
    LinearLayout mLinear;
    @ViewById(R.id.avatar)
    SimpleDraweeView mAvatar;
    @ViewById(R.id.user_name)
    TextView mUserName;
    @ViewById(R.id.introduce)
    TextInputLayout mIntroduce;
    @ViewById(R.id.my_blog)
    TextInputLayout mBlog;
    @ViewById(R.id.edit_progress_bar)
    ContentLoadingProgressBar mEditProBar;
    @ViewById(R.id.avatar_progress_bar)
    ContentLoadingProgressBar mAvatarProBar;
    @ViewById(R.id.edit_info)
    Button mEditInfoBtn;
    @ViewById(R.id.edit_avatar)
    TextView mEditAvatar;

    @AfterViews
    void init(){
        if(UserInfoUtils.isLogin(this)){
            UserBean userBean = UserInfoUtils.getUserInfo(this);
            mAvatar.setImageURI(ProcessDataUtils.getAvatar(userBean.getUid()));
        }else {
            finish();
        }
    }

    @Click(R.id.avatar)
    void onEditAvatar(){
        ImgUploadUtils.openGalleryForResult(this, OPEN_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(OPEN_GALLERY_REQUEST == requestCode){
            if(Activity.RESULT_OK == resultCode){
                String realImgPath = FileUtils.getUriPath(this, data.getData());
                if(TextUtils.isEmpty(realImgPath)){
                    finish();
                }else {
                    mAvatar.setImageURI(data.getData());
                    uploadAvatar(new File(realImgPath));
                }
            }else if(Activity.RESULT_CANCELED == resultCode){
                finish();
            }
        }
    }

    private void uploadAvatar(File imgFile){
        RequestBody imgRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
        ApiIml.getInstance(this).uploadAvatar(imgRequestBody).compose(RxUtils.<UploadAvatarResponseBean>applySchedulers())
                .compose(this.<UploadAvatarResponseBean>bindToLifecycle())
                .subscribe(new Observer<UploadAvatarResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mAvatarProBar.setVisibility(View.VISIBLE);
                        mEditAvatar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(UploadAvatarResponseBean avatarResponseBean) {
                        Snackbar.make(mLinear, "头像修改成功", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mAvatarProBar.setVisibility(View.GONE);
                        mEditAvatar.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                        Snackbar.make(mLinear, "头像上传失败", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        mAvatarProBar.setVisibility(View.GONE);
                        mEditAvatar.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Click(R.id.edit_info)
    void onEditInfo(){
        String introduce = mIntroduce.getEditText().getText().toString();
        String blog = mBlog.getEditText().getText().toString();

        ApiIml.getInstance(this)
                .editInfo(introduce, blog)
                .compose(RxUtils.<UserBean>applySchedulers())
                .subscribe(new Consumer<UserBean>() {
                    @Override
                    public void accept(UserBean userBean) throws Exception {

                    }
                });
    }

    @Click(R.id.logout)
    void onLogout(){
        CacheUtils.delete(this, Constans.KEY_USER_INFO);
        Snackbar.make(mLinear, "已退出", Snackbar.LENGTH_LONG).show();
    }

    @Click(R.id.clear_cache)
    void onClearCache(){
        Fresco.getImagePipeline().clearCaches();
        Snackbar.make(mLinear, "缓存已被清除", Snackbar.LENGTH_LONG).show();
    }

    @Click(R.id.about)
    void onAbout(){
        StartUtils.startAbout(this);
    }
}
