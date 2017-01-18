package com.x.vscam.userdetail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.ui.ImgFlowView;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.UserInfoUtils;
import com.x.vscam.global.utils.Utils;

import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.TextView;

@EActivity(R.layout.activity_user_detail)
public class UserDetailActivity extends BaseActivity {

    private static final String TAG = "user_img_flow";

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.avatar)
    SimpleDraweeView mAvatar;
    @ViewById(R.id.img_flow)
    ImgFlowView mImgFlowView;
    @ViewById(R.id.user_name)
    TextView mUserName;
    @ViewById(R.id.description)
    TextView mDescription;

    @AfterViews
    void init(){
        initFrescoShareElement();

        if(UserInfoUtils.isLogin(this)){
            UserBean userBean = UserInfoUtils.getUserInfo(this);
            mAvatar.setImageURI(ProcessDataUtils.getAvatar(userBean));
            mUserName.setText(userBean.getName());
            mDescription.setText(userBean.getDes());
            mImgFlowView.init(TAG, userBean.getUid(), true);
            Utils.setDisplayHomeAsUp(this, mToolbar);
        }else {
            finish();
        }
    }

    private void initFrescoShareElement(){
        Window window = getWindow();
        window.setSharedElementEnterTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP
                        , ScalingUtils.ScaleType.CENTER_CROP));
        window.setSharedElementReturnTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP
                        , ScalingUtils.ScaleType.CENTER_CROP));
    }
}
