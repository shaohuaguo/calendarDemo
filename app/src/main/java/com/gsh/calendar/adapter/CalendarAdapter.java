package com.gsh.calendar.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by damon on 1/5/16.
 * 日历视图的适配器
 */
public class CalendarAdapter<v extends View> extends PagerAdapter {
    private final String TAG = "CalendarViewAdapter";
    public v[] views;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CalendarAdapter(v[] views) {
        super();
        this.views = views;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views[position];
        int childCount = container.getChildCount();
        if (view != null) {
            container.addView(view);
//            Log.d(TAG, "type-->>" + type + ", 日历CalendarAdapter-->>添加--子View>>" + position + ", childCount-->>" + childCount + ", view.getId-->>" + view.getId());
        }
        return view;
    }

    @Override
    public int getCount() {
        return views.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = views[position];
        if (view != null) {
//            Log.d(TAG, "type-->>" + type + ", 日历CalendarAdapter-->>移除子View-->>" + position + ", childCount-->>" + container.getChildCount() + ", view.getId-->>" + view.getId());
            container.removeView(view);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}