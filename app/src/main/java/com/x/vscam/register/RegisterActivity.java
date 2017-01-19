package com.x.vscam.register;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.x.vscam.R;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.Utils;
import com.x.vscam.login.LoginErrorException;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import ykooze.ayaseruri.codesslib.others.InputMethodUtils;
import ykooze.ayaseruri.codesslib.rx.RxUtils;
import ykooze.ayaseruri.codesslib.ui.BabushkaText;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.nick)
    TextInputLayout mNick;
    @ViewById(R.id.email)
    TextInputLayout mEmail;
    @ViewById(R.id.password)
    TextInputLayout mPassword;
    @ViewById(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @ViewById(R.id.user_agreement)
    BabushkaText mUserAgreement;
    @ViewById(R.id.user_agreement_check)
    CheckBox mUserAgreementCheck;
    @ViewById(R.id.root)
    LinearLayout mRoot;

    @AfterViews
    void init(){
        mUserAgreement.addPiece(new BabushkaText.Piece.Builder("同意")
                .textColor(Color.parseColor("#FF878787"))
                .build());

        mUserAgreement.addPiece(new BabushkaText.Piece.Builder(" 用户协议")
                .textColor(ykooze.ayaseruri.codesslib.others.Utils.getColor(this, R.color.colorAccent))
                .build());

        mUserAgreement.display();

        Utils.setDisplayHomeAsUp(this, mToolbar);
    }

    @Click(R.id.register_btn)
    void onRegister(){
        if(!mUserAgreementCheck.isChecked()){
            Utils.getSnackBar(this, "须先同意用户协议").show();
            return;
        }

        String nick = mNick.getEditText().getText().toString();
        if(TextUtils.isEmpty(nick)){
            Utils.getSnackBar(this, "请填写昵称").show();
            return;
        }

        String email = mEmail.getEditText().getText().toString();
        if(TextUtils.isEmpty(email)){
            Utils.getSnackBar(this, "请填写邮箱").show();
            return;
        }

        String pass = mPassword.getEditText().getText().toString();
        if(TextUtils.isEmpty(pass)){
            Utils.getSnackBar(this, "请填写密码").show();
            return;
        }

        ApiIml.getInstance(this).register(nick, email, pass)
                .compose(this.<UserBean>bindToLifecycle())
                .compose(RxUtils.<UserBean>applySchedulers())
                .flatMap(new Function<UserBean, ObservableSource<UserBean>>() {
                    @Override
                    public ObservableSource<UserBean> apply(UserBean userBean) throws Exception {
                        if(TextUtils.isEmpty(userBean.getError())){
                            return Observable.just(userBean);
                        }
                        return Observable.error(new LoginErrorException(userBean.getError()));
                    }
                })
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
                        if(e instanceof LoginErrorException){
                            InputMethodUtils.hide(RegisterActivity.this, mRoot);
                            Utils.getSnackBar(RegisterActivity.this, e.getMessage()).show();
                        }
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Click(R.id.user_agreement)
    void onUserAgreement(){
        ykooze.ayaseruri.codesslib.others.Utils.userLocalBorwer(this, "http://vscam.co/privacy.html");
    }
}
