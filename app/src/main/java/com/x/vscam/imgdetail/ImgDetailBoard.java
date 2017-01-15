package com.x.vscam.imgdetail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.x.vscam.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wufeiyang on 2017/1/13.
 */
@EViewGroup(R.layout.view_img_detail_board)
public class ImgDetailBoard extends LinearLayout{

    @ViewById(R.id.user_name)
    TextView mUserName;
    @ViewById(R.id.time)
    TextView mTime;
    @ViewById(R.id.aperture)
    TextView mAperture;
    @ViewById(R.id.ios)
    TextView mIos;
    @ViewById(R.id.fliter_name)
    TextView mFliterName;
    @ViewById(R.id.description)
    TextView mDescription;

    @AfterViews
    void init(){
        setOrientation(VERTICAL);
    }

    public ImgDetailBoard(Context context) {
        super(context);
    }

    public ImgDetailBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUserName(String userName) {
        mUserName.setText(userName);
    }

    public void setTime(String time) {
        mTime.setText(time);
    }

    public void setAperture(String aperture) {
        mAperture.setText("f/" + aperture);
    }

    public void setios(String ios) {
        this.mIos.setText(TextUtils.isEmpty(ios) ? "--" : ios);
    }

    public void setFliterName(String fliterName) {
        this.mFliterName.setText(TextUtils.isEmpty(fliterName) ? "--" : fliterName);
    }

    public void setDescription(String description) {
        this.mDescription.setText(description);
    }
}
