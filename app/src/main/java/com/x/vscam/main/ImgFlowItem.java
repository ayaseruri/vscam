package com.x.vscam.main;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.utils.StartUtils;

import android.content.Context;
import android.text.TextUtils;
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
    public void onBindData(ImgFlowBean.GridsBean data, int postion) {
        ViewGroup.LayoutParams layoutParams = mImg.getLayoutParams();
        layoutParams.height = (int) ((LocalDisplay.SCREEN_WIDTH_PIXELS - 3 * mImgFlowSpacing) / 2.0f * data.getScale());

        String imgUrl;
        if(TextUtils.isEmpty(data.getWbpid())){
            imgUrl = String.format(Constans.OG_IMG_URL_PRE, data.getOrigin());
        }else {
            imgUrl = String.format(Constans.WB_IMG_URL_PRE, data.getWbpid());
        }
        mImg.setImageURI(imgUrl);
        mUserName.setText(null == data.getUsersBean() ? "" : data.getUsersBean().getName());

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                StartUtils.startImgDetail(getContext());
            }
        });
    }
}
