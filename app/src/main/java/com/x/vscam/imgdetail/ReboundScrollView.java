package com.x.vscam.imgdetail;

import com.orhanobut.logger.Logger;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ReboundScrollView extends ScrollView {

    private static final short ANIMATION_TIME = 300;
    private static final float MOVE_FACTOR = 0.5f;

    private View mContentView;
    private Rect mOrgContentRect = new Rect();
    private boolean isContentMoved;
    private float mStartY;
    private IOnReBound mIOnReBound;

    public ReboundScrollView(Context context) {
        super(context);
    }

    public ReboundScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() > 0){
            mContentView = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(null != mContentView){
            mOrgContentRect.set(mContentView.getLeft(), mContentView.getTop()
                    ,mContentView.getRight(), mContentView.getBottom());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(null != mContentView){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mStartY = ev.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    if(!isContentMoved){
                        break;
                    }

                    TranslateAnimation animation = new TranslateAnimation(0, 0, mContentView.getTop(), mOrgContentRect.top);
                    animation.setDuration(ANIMATION_TIME);
                    mContentView.startAnimation(animation);

                    mContentView.layout(mOrgContentRect.left, mOrgContentRect.top
                            , mOrgContentRect.right, mOrgContentRect.bottom);

                    isContentMoved = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Logger.d("getScrollY:  " + getScrollY() );

                    boolean isShouldPullUp = isShouldPullUp(), isShouldPullDown = isShouldPullDown();

                    if(!isShouldPullUp && !isShouldPullDown){
                        mStartY = ev.getY();
                        break;
                    }

                    float moveY = ev.getY() - mStartY;
                    if((isShouldPullUp && moveY < 0) || (isShouldPullDown && moveY > 0)){
                        int offset = (int) (moveY * MOVE_FACTOR);
                        mContentView.layout(mOrgContentRect.left, mOrgContentRect.top + offset
                                , mOrgContentRect.right, mOrgContentRect.bottom + offset);
                        isContentMoved = true;

                        if(null != mIOnReBound){
                            if(isShouldPullUp && moveY < 0){
                                mIOnReBound.onOverScrollTop(Math.abs(offset));
                            }else if(isShouldPullDown && moveY > 0){
                                mIOnReBound.onOverScrollBottom(Math.abs(offset));
                            }
                        }

                        return true;
                    }

                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldPullDown(){
        return getScrollY() == 0;
    }

    private boolean isShouldPullUp(){
        return  mContentView.getMeasuredHeight() == getHeight() + getScrollY();
    }

    public void setIOnReBound(IOnReBound IOnReBound) {
        mIOnReBound = IOnReBound;
    }

    public interface IOnReBound{
        void onOverScrollTop(int pix);
        void onOverScrollBottom(int pix);
    }
}
