package com.x.vscam.main;

import com.orhanobut.logger.Logger;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import ykooze.ayaseruri.codesslib.others.Utils;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class FabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton>{

    private int mToolbarHeight;

    public FabBehavior(Context context, AttributeSet attrs) {
        super();
        mToolbarHeight = Utils.getThemeToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return dependency instanceof AppBarLayout || dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if(dependency instanceof AppBarLayout){
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = child.getHeight() + fabBottomMargin;
            float ratio = dependency.getY() / mToolbarHeight;
            child.setTranslationY(- distanceToScroll * ratio);
            return true;
        }

        if (dependency instanceof Snackbar.SnackbarLayout) {
            float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
            child.setTranslationY(translationY);
            return true;
        }

        return false;
    }
}
