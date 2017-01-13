package com.x.vscam.main;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.StartUtils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
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

    @ViewById(R.id.img)
    SimpleDraweeView mImg;
    @ViewById(R.id.user_name)
    TextView mUserName;
    @DimensionPixelSizeRes(R.dimen.img_flow_spacing)
    int mImgFlowSpacing;

    public ImgFlowItem(Context context) {
        super(context);
    }

    public ImgFlowItem(Context context, AttributeSet attrs) {
        super(context, attrs);
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
