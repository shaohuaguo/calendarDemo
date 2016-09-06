package com.gsh.calendar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class InnerScrollView extends ScrollView {
    private final String TAG = "InnerScrollView";

    /**
     * 判断是否需要拦截
     */
    public boolean isIntercept = false;

    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "onInterceptTouchEvent-->>MotionEvent-->>" + Utils.actionToString(ev.getAction()) + ", isIntercept-->>" + isIntercept);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "onTouchEvent-->>MotionEvent-->>" + Utils.actionToString(ev.getAction()) + ", isIntercept-->>" + isIntercept + ", getScrollY-->>" + getScrollY());
        boolean isFuck = isIntercept;
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            isIntercept = false;
        }
        if (isFuck) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "dispatchTouchEvent-->>MotionEvent-->>" + Utils.actionToString(ev.getAction()) + ", isIntercept-->>" + isIntercept);
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.d(TAG, "onLayout-->>getWidth-->>" + getWidth() + ", height-->>" + getHeight());
    }
}