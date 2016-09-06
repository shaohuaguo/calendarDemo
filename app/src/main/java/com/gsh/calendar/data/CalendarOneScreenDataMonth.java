package com.gsh.calendar.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.gsh.calendar.widget.CalendarConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by damon on 1/8/16.
 * 日历月视图 一屏幕的数据
 */
public class CalendarOneScreenDataMonth implements Serializable {

    private int year;

    @CalendarConstant.Month
    private int month;

    /**
     * 选中的天的位置
     */
    @IntRange(from = 1, to = 42)
    private int selectedPosition;

    /**
     * 只有在CalendarConstant.CALENDAR_START --经期最后一天所在的月份 的范围内才是有效的
     */
    @CalendarConstant.PagerValidState
    private int invalidState;

    /**
     * key: 为约定好的7列6行
     * 只提供get方法, 不需要判断为空
     */
    @NonNull
    Map<Integer, DayData> mapDay = new HashMap<>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @NonNull
    public Map<Integer, DayData> getMapDay() {
        return mapDay;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getInvalidState() {
        return invalidState;
    }

    public void setInvalidState(int invalidState) {
        this.invalidState = invalidState;
    }
}
