package com.x.vscam.main;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.ui.ImgFlowView;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.StartUtils;
import com.x.vscam.global.utils.UserInfoUtils;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import ykooze.ayaseruri.codesslib.ui.CommonRecyclerView;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final String TAG = "main_img_flow";

    @ViewById(R.id.activity_main)
    View mRoot;
    @ViewById(R.id.recycler)
    CommonRecyclerView mRecyclerView;
    @ViewById(R.id.avatar)
    SimpleDraweeView mAvatar;
    @ViewById(R.id.img_flow)
    ImgFlowView mImgFlowView;

    @Override
    protected void onResume() {
        super.onResume();
        GenericDraweeHierarchy hierarchy = mAvatar.getHierarchy();
        if(UserInfoUtils.isLogin(this)){
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            hierarchy.setRoundingParams(RoundingParams.asCircle().setRoundAsCircle(true));
            mAvatar.setImageURI(ProcessDataUtils.getAvatar(UserInfoUtils.getUserInfo(this)));
        }else {
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            hierarchy.setRoundingParams(RoundingParams.asCircle().setRoundAsCircle(false));
            mAvatar.setImageURI(Uri.parse("res:///" + R.mipmap.ic_login));
        }
    }

    @AfterViews
    void init(){
        mImgFlowView.init(TAG, 0);
    }

    @Click(R.id.avatar)
    void onAvatar(){
        if(UserInfoUtils.isLogin(this)){
            StartUtils.startUserDetail(this
                    , ActivityOptionsCompat.makeSceneTransitionAnimation(this
                            , mAvatar, getString(R.string.avatar_transition_name)));
        }else {
            StartUtils.startLogin(this
                    , ActivityOptionsCompat.makeSceneTransitionAnimation(this
                            , mAvatar, getString(R.string.avatar_transition_name)));
        }
    }

    @Click(R.id.settings)
    void onSettings(){
        if(UserInfoUtils.isLogin(this)){
            StartUtils.startSettings(this);
        }else {
            Snackbar.make(mRoot, "需要登录", Snackbar.LENGTH_LONG).show();
        }
    }

    @Click(R.id.fab)
    void onFab(){
        if(UserInfoUtils.isLogin(this)){
            StartUtils.startUpload(this);
        }else {
            StartUtils.startLogin(this
                    , ActivityOptionsCompat.makeSceneTransitionAnimation(this
                            , mAvatar, getString(R.string.avatar_transition_name)));
        }
    }
}
