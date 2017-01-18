package com.x.vscam.global.ui;

import org.androidannotations.annotations.EViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.StartUtils;
import com.x.vscam.main.ImgFlowBean;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ykooze.ayaseruri.codesslib.adapter.RecyclerAdapter;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

/**
 * Created by wufeiyang on 2017/1/12.
 */
@EViewGroup(R.layout.item_img_flow)
public class ImgFlowItem extends RecyclerAdapter.Item<ImgFlowBean.GridsBean> {

    private SimpleDraweeView mImg;
    private TextView mUserName;
    private int mImgFlowSpacing;

    public ImgFlowItem(Context context) {
        super(context);
        initView();
    }

    public ImgFlowItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.item_img_flow, this, true);
        mImg = (SimpleDraweeView) findViewById(R.id.img);
        mUserName = (TextView) findViewById(R.id.user_name);
        mImgFlowSpacing = getContext().getResources().getDimensionPixelSize(R.dimen.img_flow_spacing);
    }

    public void setUserName(int visibility){
        mUserName.setVisibility(visibility);
    }

    @Override
    public void onBindData(final ImgFlowBean.GridsBean data, int postion) {
        ViewGroup.LayoutParams layoutParams = mImg.getLayoutParams();
        layoutParams.height = (int) ((LocalDisplay.SCREEN_WIDTH_PIXELS - 3 * mImgFlowSpacing) / 2.0f * data.getScale());

        mImg.setImageURI(ProcessDataUtils.getImgUrlS(data));
        mUserName.setText(data.getUserName());

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                StartUtils.startImgDetail(getContext()
                        , data
                        , ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), mImg,
                                getContext().getString(R.string.img_transition_name)));
            }
        });
    }
}
