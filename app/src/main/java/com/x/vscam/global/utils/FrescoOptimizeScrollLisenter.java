package com.x.vscam.global.utils;

import com.facebook.drawee.backends.pipeline.Fresco;

import android.support.v7.widget.RecyclerView;

/**
 * Created by wufeiyang on 2017/1/17.
 */

public class FrescoOptimizeScrollLisenter extends RecyclerView.OnScrollListener {

    private int mPreScrollState;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE:
                if(Fresco.getImagePipeline().isPaused()){
                    Fresco.getImagePipeline().resume();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                Fresco.getImagePipeline().pause();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if(RecyclerView.SCROLL_STATE_DRAGGING == mPreScrollState){
                    Fresco.getImagePipeline().pause();
                }else {
                    if(Fresco.getImagePipeline().isPaused()){
                        Fresco.getImagePipeline().resume();
                    }
                }
                break;
            default:
                break;
        }

        mPreScrollState = newState;
    }
}
