package com.gsh.calendar.data;

import com.gsh.calendar.widget.CalendarConstant;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by damon on 1/7/16.
 */
public class DateData implements Serializable {

    private int year;
    @CalendarConstant.Month
    private int month;
    private int day;
    @CalendarConstant.Week
    private int week;

    /**
     * 该时间点的unix时间戳(作用了只是为了方便获取)
     * 对准到00:00点的unix时间戳
     */
    private long unix;

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getUnix() {
        return unix;
    }

    public void setUnix(long unix) {
        this.unix = unix;
    }


    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return "DateData{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", week=" + week +
                ", unix=" + unix +
                ", unix格式化=" + new Date(unix * 1000L) +
                '}';
    }
}
