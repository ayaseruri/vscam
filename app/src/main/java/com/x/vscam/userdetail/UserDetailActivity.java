package com.x.vscam.userdetail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.ui.ImgFlowView;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.UserInfoUtils;

import android.support.v7.widget.Toolbar;

@EActivity(R.layout.activity_user_detail)
public class UserDetailActivity extends BaseActivity {

    private static final String TAG = "user_img_flow";

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.avatar)
    SimpleDraweeView mAvatar;
    @ViewById(R.id.img_flow)
    ImgFlowView mImgFlowView;

    @AfterViews
    void init(){
        if(UserInfoUtils.isLogin(this)){
            UserBean userBean = UserInfoUtils.getUserInfo(this);
            mAvatar.setImageURI(ProcessDataUtils.getAvatar(userBean));
            mImgFlowView.init(TAG, userBean.getUid());
        }else {
            finish();
        }
    }
}
