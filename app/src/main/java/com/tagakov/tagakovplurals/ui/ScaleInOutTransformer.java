package com.tagakov.tagakovplurals.ui;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Тагаков Владимир on 01.02.2015.
 */
public class ScaleInOutTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        view.setPivotX(position < 0 ? 0 : view.getWidth());
        view.setPivotY(view.getHeight() / 2f);
        float scale = position < 0 ? 1f + position : 1f - position;
        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}