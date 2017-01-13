package com.x.vscam.imgviewer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.imagepipeline.image.ImageInfo;
import com.x.vscam.R;
import com.x.vscam.global.Constans;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.Window;
import me.relex.photodraweeview.PhotoDraweeView;
import ykooze.ayaseruri.codesslib.rx.RxActivity;

@EActivity(R.layout.activity_img_viewer)
public class ImgViewerActivity extends RxActivity {

    @ViewById(R.id.img)
    PhotoDraweeView mImg;

    @AfterViews
    void init(){
        initFrescoShareElement();

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(Uri.parse(getIntent().getStringExtra(Constans.KEY_IMG_PATH)));
        controller.setOldController(mImg.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mImg == null) {
                    return;
                }
                mImg.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        mImg.setController(controller.build());
    }

    private void initFrescoShareElement(){
        Window window = getWindow();
        window.setSharedElementEnterTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP
                        , ScalingUtils.ScaleType.FIT_CENTER));
        window.setSharedElementReturnTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER
                        , ScalingUtils.ScaleType.CENTER_CROP));
    }
}
