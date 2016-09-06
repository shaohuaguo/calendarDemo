package com.gsh.calendar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by damon on 1/6/16.
 */
public class CalendarViewPager extends ViewPager  {
    private final String TAG = "CalendarViewPager";

    public CalendarViewPager(Context context) {
        super(context);
        init();
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.d(TAG, "onMeasure-->>");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.d(TAG, "onLayout-->>");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
