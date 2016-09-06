package com.gsh.calendar.data;

import com.gsh.calendar.widget.CalendarConstant;

import java.io.Serializable;

/**
 * Created by damon on 1/7/16.
 * 日单元格的数据
 */
public class DayData extends DateData implements Serializable {


    /**
     * text的Top的文本
     */
    @CalendarConstant.TopType
    private int topType;

    /**
     * text的Bottom是否有圆点
     */
    private boolean hasCircleBottom;

    /**
     * 日历中 "日" 的类型
     */
    @CalendarConstant.DayType
    private int dayType;

    /**
     * 日历中 "日" 是否可以点击
     * true: 可点击
     * false: 不可点击,且字体颜色为灰色
     */
    private boolean isClickable;

    public boolean isHasCircleBottom() {
        return hasCircleBottom;
    }

    public void setHasCircleBottom(boolean hasCircleBottom) {
        this.hasCircleBottom = hasCircleBottom;
    }

    public int getTopType() {
        return topType;
    }

    public void setTopType(int topType) {
        this.topType = topType;
    }
    @CalendarConstant.DayType
    public int getDayType() {
        return dayType;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    @Override
    public String toString() {
        return "DayData{" +
                "topType=" + topType +
                ", hasCircleBottom=" + hasCircleBottom +
                ", dayType=" + dayType +
                ", isClickable=" + isClickable +
                "} " + super.toString();
    }
}
