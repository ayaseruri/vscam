package com.x.vscam.detail;

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

public class ScaleScrollView extends ScrollView {

    private static final short ANIMATION_TIME = 500;
    private static final float MOVE_FACTOR = 0.5f;

    private View mContentView;
    private Rect mOrgContentRect = new Rect();
    private boolean mCanPullUp, mCanPullDown, isContentMoved;
    private float mStartY;

    public ScaleScrollView(Context context) {
        super(context);
    }

    public ScaleScrollView(Context context, AttributeSet attrs) {
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
                    mCanPullUp = isCanPullUp();
                    mCanPullDown = isCanPullDown();
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

                    mCanPullUp = false;
                    mCanPullDown = false;
                    isContentMoved =false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!mCanPullUp && !mCanPullDown){
                        mStartY = ev.getY();
                        mCanPullUp = isCanPullUp();
                        mCanPullDown = isCanPullDown();
                        break;
                    }

                    float moveY = ev.getY() - mStartY;

                    boolean shouldMove = (mCanPullDown && moveY >0)
                            || (mCanPullUp && moveY < 0)
                            || (mCanPullUp && mCanPullDown);

                    if(shouldMove){
                        int offset = (int) (moveY * MOVE_FACTOR);
                        mContentView.layout(mOrgContentRect.left, mOrgContentRect.top + offset
                                , mOrgContentRect.right, mOrgContentRect.bottom + offset);
                        isContentMoved = true;
                    }

                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isCanPullUp(){
        return getScrollY() == 0 ||
                mContentView.getHeight() < getHeight() + getScrollY();
    }

    private boolean isCanPullDown(){
        return  mContentView.getHeight() <= getHeight() + getScrollY();
    }
}
