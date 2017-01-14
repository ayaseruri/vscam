package com.x.vscam.login;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.ui.BaseActivity;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.linear)
    LinearLayout mLinear;
    @ViewById(R.id.email)
    TextInputLayout mEmail;
    @ViewById(R.id.password)
    TextInputLayout mPassword;
    @ViewById(R.id.login_ic)
    SimpleDraweeView mLoginIc;
    @ViewById(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;

    @AfterViews
    void init(){
        mLoginIc.setImageURI(Uri.parse("res:///" + R.mipmap.ic_login));

        initFrescoShareElement();
    }

    @Click(R.id.login_btn)
    void onLogin(){
        String email = mEmail.getEditText().getText().toString();
        if(TextUtils.isEmpty(email)){
            Snackbar.make(mLinear, "邮箱不能为空", Snackbar.LENGTH_LONG).show();
            return;
        }

        String pass = mPassword.getEditText().getText().toString();
        if(TextUtils.isEmpty(pass)){
            Snackbar.make(mLinear, "密码不能为空", Snackbar.LENGTH_LONG).show();
            return;
        }

        ApiIml.getInstance(this).login(email.trim(), pass.trim())
                .compose(RxUtils.<UserBean>applySchedulers())
                .compose(RxUtils.<UserBean>applyCache(this, Constans.KEY_USER_INFO))
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UserBean userBean) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                });
    }

    private void initFrescoShareElement(){
        Window window = getWindow();
        window.setSharedElementEnterTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_INSIDE
                        , ScalingUtils.ScaleType.FIT_CENTER));
        window.setSharedElementReturnTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER
                        , ScalingUtils.ScaleType.CENTER_INSIDE));
    }
}
