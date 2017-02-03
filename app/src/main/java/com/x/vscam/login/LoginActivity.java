package com.x.vscam.login;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.StartUtils;
import com.x.vscam.global.utils.UserInfoUtils;
import com.x.vscam.global.utils.Utils;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import ykooze.ayaseruri.codesslib.others.InputMethodUtils;
import ykooze.ayaseruri.codesslib.others.ToastUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
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
    @ViewById(R.id.login_btn)
    Button mLoginBtn;

    @AfterViews
    void init(){
        mLoginIc.setImageURI(Uri.parse("res:///" + R.mipmap.ic_login));
        Utils.setDisplayHomeAsUp(this, mToolbar);
        initFrescoShareElement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserInfoUtils.isLogin(LoginActivity.this)){
            finish();
        }
    }

    @Click(R.id.login_btn)
    void onLogin(){
        String email = mEmail.getEditText().getText().toString();
        if(TextUtils.isEmpty(email)){
            Snackbar.make(mLinear, "请填写邮箱", Snackbar.LENGTH_LONG).show();
            return;
        }

        String pass = mPassword.getEditText().getText().toString();
        if(TextUtils.isEmpty(pass)){
            Snackbar.make(mLinear, "请填写密码", Snackbar.LENGTH_LONG).show();
            return;
        }

        ApiIml.getInstance(this).login(email.trim(), pass.trim())
                .compose(RxUtils.<UserBean>applySchedulers())
                .flatMap(new Function<UserBean, ObservableSource<UserBean>>() {
                    @Override
                    public ObservableSource<UserBean> apply(UserBean userBean) throws Exception {
                        if(TextUtils.isEmpty(userBean.getError())){
                            UserInfoUtils.saveUserInfo(LoginActivity.this, userBean);
                            return Observable.just(userBean);
                        }else {
                            return Observable.error(new LoginErrorException(userBean.getError()));
                        }
                    }
                })
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        ToastUtils.showTost(LoginActivity.this, ToastUtils.TOAST_CONFIRM, "登陆成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if(e instanceof LoginErrorException){
                            InputMethodUtils.hide(LoginActivity.this, mLinear);
                            Snackbar.make(mLinear, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                });
    }

    @Click(R.id.register)
    void onRegister(){
        StartUtils.startRegister(this, ActivityOptionsCompat.makeSceneTransitionAnimation(this
                , Pair.<View, String>create(mEmail, getString(R.string.email_transition_name))
                , Pair.<View, String>create(mPassword, getString(R.string.pass_transition_name))
                , Pair.<View, String>create(mLoginBtn, getString(R.string.register_btn_transition_name))));
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
