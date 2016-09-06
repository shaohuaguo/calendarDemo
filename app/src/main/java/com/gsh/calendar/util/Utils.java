package com.gsh.calendar.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.UiThread;
import android.view.MotionEvent;

import com.gsh.calendar.MainActivity;
import com.gsh.calendar.R;
import com.gsh.calendar.widget.CalendarConstant;

/**
 * Created by damon on 1/5/16.
 */
public class Utils {
    public static int pxValue2dipValue(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dipValue2PxValue(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将触摸的Action转换成String
     *
     * @param action
     * @return
     */
    public static String actionToString(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";
            case MotionEvent.ACTION_OUTSIDE:
                return "ACTION_OUTSIDE";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_HOVER_MOVE:
                return "ACTION_HOVER_MOVE";
            case MotionEvent.ACTION_SCROLL:
                return "ACTION_SCROLL";
            case MotionEvent.ACTION_HOVER_ENTER:
                return "ACTION_HOVER_ENTER";
            case MotionEvent.ACTION_HOVER_EXIT:
                return "ACTION_HOVER_EXIT";
        }
        int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                return "ACTION_POINTER_DOWN(" + index + ")";
            case MotionEvent.ACTION_POINTER_UP:
                return "ACTION_POINTER_UP(" + index + ")";
            default:
                return Integer.toString(action);
        }
    }

    /**
     * 更新title的相关颜色
     *
     * @param mainActivity mainActivity
     * @param dayType      选中天的类型
     */
    @UiThread
    public static void setTitleColor(MainActivity mainActivity, @CalendarConstant.DayType int dayType) {

        boolean isSelected = true;
        int bgColor = 0;
        int bgBorderColor = 0;
        Drawable bgDrawable = null;
        switch (dayType) {
            case CalendarConstant.DAY_TYPE_SAFE:
                isSelected = false;
                bgDrawable = mainActivity.getResources().getDrawable(R.drawable.shape_border_bg);
                bgColor = mainActivity.getResources().getColor(R.color.color_c3);
                break;
            case CalendarConstant.DAY_TYPE_MENS_START:
            case CalendarConstant.DAY_TYPE_MENS_END:
            case CalendarConstant.DAY_TYPE_MENS_ING:
            case CalendarConstant.DAY_TYPE_MENS_ONLYONE:
                bgColor = mainActivity.getResources().getColor(R.color.color_mens);
                bgBorderColor = mainActivity.getResources().getColor(R.color.color_mens);

                break;
            case CalendarConstant.DAY_TYPE_EASY_START:
            case CalendarConstant.DAY_TYPE_EASY_END:
            case CalendarConstant.DAY_TYPE_EASY_ING:
            case CalendarConstant.DAY_TYPE_EASY_ONLYONE0:
                bgColor = mainActivity.getResources().getColor(R.color.color_easy);
                bgBorderColor = mainActivity.getResources().getColor(R.color.color_easy);
                break;
            case CalendarConstant.DAY_TYPE_EASY_ONLYONE1:
            case CalendarConstant.DAY_TYPE_OVULATE:
                bgColor = mainActivity.getResources().getColor(R.color.color_ovulation);
                bgBorderColor = mainActivity.getResources().getColor(R.color.color_ovulation);
                break;
        }
        //返回键
        mainActivity.findViewById(R.id.title_back).setSelected(isSelected);
        //日历内容
        mainActivity.findViewById(R.id.calendar_title_date).setSelected(isSelected);
        //向下箭头("日历内容"右侧)
        mainActivity.findViewById(R.id.calendar_title_down).setSelected(isSelected);
        //今天按钮
        mainActivity.findViewById(R.id.calendar_title_today).setSelected(isSelected);
        //帮助按钮
        mainActivity.findViewById(R.id.calendar_title_help).setSelected(isSelected);

        mainActivity.findViewById(R.id.sunday).setSelected(isSelected);
        mainActivity.findViewById(R.id.monday).setSelected(isSelected);
        mainActivity.findViewById(R.id.tuesday).setSelected(isSelected);
        mainActivity.findViewById(R.id.wednesday).setSelected(isSelected);
        mainActivity.findViewById(R.id.thursday).setSelected(isSelected);
        mainActivity.findViewById(R.id.friday).setSelected(isSelected);
        mainActivity.findViewById(R.id.saturday).setSelected(isSelected);

        //title布局颜色
        mainActivity.findViewById(R.id.calendar_title_rl).setBackgroundColor(bgColor);
        //week布局颜色
        mainActivity.findViewById(R.id.calendar_week_ll).setBackgroundColor(bgColor);
        if (bgDrawable != null) {
            //安全期
            mainActivity.findViewById(R.id.week_border).setBackground(bgDrawable);
        } else {
            //非安全期
            mainActivity.findViewById(R.id.week_border).setBackgroundColor(bgBorderColor);
        }
    }

}
