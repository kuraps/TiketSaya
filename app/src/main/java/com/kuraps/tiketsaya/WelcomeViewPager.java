package com.kuraps.tiketsaya;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class WelcomeViewPager extends ViewPager{

    private Boolean disable = false;

    public WelcomeViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public WelcomeViewPager(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !disable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !disable && super.onTouchEvent(event);
    }

    public void disableScroll(Boolean disable) {
        this.disable = disable;
    }

}
