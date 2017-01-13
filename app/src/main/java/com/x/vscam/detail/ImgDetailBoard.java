package com.x.vscam.detail;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.x.vscam.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wufeiyang on 2017/1/13.
 */
@EViewGroup(R.layout.view_img_detail_board)
public class ImgDetailBoard extends RelativeLayout{

    @ViewById(R.id.user_name)
    TextView mUserName;
    @ViewById(R.id.time)
    TextView mTime;
    @ViewById(R.id.aperture)
    TextView mAperture;

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
        mAperture.setText(aperture);
    }
}
