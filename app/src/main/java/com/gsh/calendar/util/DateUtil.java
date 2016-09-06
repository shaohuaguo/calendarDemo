package com.gsh.calendar.util;


import android.content.Context;
import android.util.Log;


import com.gsh.calendar.R;
import com.gsh.calendar.data.DayData;
import com.gsh.calendar.widget.CalendarConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DateUtil {

    private static final String TAG = "DateUtil";
    // public static String[] weekName = { "周日", "周一", "周二", "周三", "周四", "周四",
    // "周六" };

    public static int getMonthDays(int year, @CalendarConstant.Month int month) {
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }
        try {
            days = arr[month];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }


    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }


    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    /**
     * 获取某个月
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public static int getWeekDayFromDate(int year, @CalendarConstant.Month int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static Date getDateFromString(int year, @CalendarConstant.Month int month) {
        int showMonth = month + 1;
        String dateString = year + "-" + (showMonth > 9 ? showMonth : ("0" + showMonth))
                + "-" + 01;
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     * @Title: isUnreach
     * @Description: 判断某一个是否为未来的日期
     */
    public static boolean isUnreach(int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        Calendar mCalendar2 = Calendar.getInstance(Locale.getDefault());
        mCalendar2.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar2.set(Calendar.MINUTE, 0);
        mCalendar2.set(Calendar.SECOND, 0);
        mCalendar2.set(Calendar.MILLISECOND, 0);
        return mCalendar.getTimeInMillis() > mCalendar2.getTimeInMillis();
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return true 表示大于timeUnix
     * @Title: isWhichMax
     * @Description: 比较两个时间的大小
     */
    public static boolean isWhichMax(int year, int month, int day, long timeUnix) {
        Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        return mCalendar.getTimeInMillis() > timeUnix * 1000L;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     * @Title: getTimeUnix
     * @Description: 根据年月日获取那一天的0点的unix时间戳, 单位s
     */
    public static long getTimeUnix(int year, int month, int day) {
        //此方法获取的00:00的时间不受夏令时的影响
        Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        return (int) (mCalendar.getTimeInMillis() / 1000L);
    }

    public static long get0000Unix1(long unix) {
        //此方法获取的00:00的时间不受夏令时的影响
        Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
        mCalendar.setTimeInMillis(unix * 1000L);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        return (int) (mCalendar.getTimeInMillis() / 1000L);
    }

    /**
     * @return
     * @Title: getTodayUnix
     * @Description: 获取今天的UNIX时间戳
     */
    public static long getTodayUnix() {
        Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        return mCalendar.getTimeInMillis() / 1000L;
    }

    public static int getArrFromUnix(long unix, Map<Integer, DayData> mapDay) {
        if (mapDay == null || mapDay.size() == 0) {
            Log.e("test-->>", "参数异常");
            return -1;
        }
        for (Map.Entry<Integer, DayData> entry : mapDay.entrySet()) {
            if (entry.getValue().getUnix() == unix) {
                return entry.getKey();
            }
        }
        Log.e("test-->>", "无找到对应数据");
        return -1;
    }

    /**
     * 获取两个日期的月份差
     *
     * @param startUnix 起始时间的unix
     * @param endUnix   结束时间的unix
     * @return
     */
    public static int getPagerMonth(long startUnix, long endUnix) {

        if (endUnix < startUnix) {
            throw new RuntimeException("参数异常");
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(startUnix * 1000L);

        int monthStart = calendarStart.get(Calendar.YEAR) * 12 + calendarStart.get(Calendar.MONTH);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(endUnix * 1000L);
        int monthEnd = calendarEnd.get(Calendar.YEAR) * 12 + calendarEnd.get(Calendar.MONTH);

        int result = Math.abs(monthEnd - monthStart) + 1;
        Log.d(TAG, "月份总个数-->>" + result);
        return result;
    }

    /**
     * 获取选中月的index
     *
     * @param startUnix     起始时间的unix
     * @param selectedMonth 选中月的unix
     * @return
     */
    public static int getSelectedPagerMonthIndex(long startUnix, long selectedMonth) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(startUnix * 1000L);

        int monthStart = calendarStart.get(Calendar.YEAR) * 12 + calendarStart.get(Calendar.MONTH);


        Calendar calendarSelected = Calendar.getInstance();
        calendarSelected.setTimeInMillis(selectedMonth * 1000L);
        int monthSelected = calendarSelected.get(Calendar.YEAR) * 12 + calendarSelected.get(Calendar.MONTH);


        int result = Math.abs(monthSelected - monthStart);
        Log.d(TAG, "当前月的index-->>" + result);
        return result;
    }

    public static int getSelectedPagerWeekIndex(long startUnix, long selectedMonthUnix) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(startUnix * 1000L);
//        calendarStart.set(Calendar.DAY_OF_MONTH, 1);

        Calendar calendarSelected = Calendar.getInstance();
        calendarSelected.setTimeInMillis(selectedMonthUnix * 1000L);

        if (calendarSelected.getTimeInMillis() < calendarStart.getTimeInMillis()) {
            Log.d(TAG, "选中-->>" + calendarSelected.getTimeInMillis() / 1000L + ", 起始-->>" + calendarStart.getTimeInMillis() / 1000L);
            throw new RuntimeException("参数异常");
        }

        int weekTol = 52;
//        Log.d(TAG, "起始日期所在的星期-->>" + calendarStart.get(Calendar.WEEK_OF_YEAR) + ", 起始时间-->>" + calendarStart.getTime().toString());
        int weekStart = calendarStart.get(Calendar.YEAR) * weekTol + calendarStart.get(Calendar.WEEK_OF_YEAR);


//        Log.d(TAG, "选中日期所在的星期-->>" + calendarSelected.get(Calendar.WEEK_OF_YEAR) + ", 选中时间-->>" + calendarSelected.getTime().toString());
        int weekSelected = calendarSelected.get(Calendar.YEAR) * weekTol + calendarSelected.get(Calendar.WEEK_OF_YEAR);

        int result = Math.abs(weekStart - weekSelected);
        Log.d(TAG, "当星期的index-->>" + result);
        return result;
    }

    /**
     * 获取两个日期的星期差
     *
     * @param startUnix 起始时间的unix
     * @param endUnix   结束时间的unix
     * @return
     */
    public static int getPagerWeek(long startUnix, long endUnix) {

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(startUnix * 1000L);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(endUnix * 1000L);

        if (calendarEnd.getTimeInMillis() < calendarStart.getTimeInMillis()) {
            throw new RuntimeException("参数异常");
        }
        int weekTol = 52;
//        Log.d(TAG, "起始日期所在的星期0-->>" + calendarStart.get(Calendar.WEEK_OF_YEAR) + ", 起始时间-->>" + calendarStart.getTime().toString());
        int weekStart = calendarStart.get(Calendar.YEAR) * weekTol + calendarStart.get(Calendar.WEEK_OF_YEAR);


        int weekEnd = calendarEnd.get(Calendar.YEAR) * weekTol + calendarEnd.get(Calendar.WEEK_OF_YEAR);
//        Log.d(TAG, "结束日期所在的星期-->>" + calendarEnd.get(Calendar.WEEK_OF_YEAR) + ", 选中时间-->>" + calendarEnd.getTime().toString());
        int result = Math.abs(weekStart - weekEnd) + 1;
        Log.d(TAG, "总星期个数-->>" + result);
        return result;
    }

    /**
     * 获得unix
     *
     * @param time 单位:毫秒
     */
    public static long getTimeUnix(long time) {
        return time / 1000L;
    }

    public static int getWeek(long unix) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unix * 1000L);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param unix unix
     * @return 打印出unix的时间信息
     */
    public static String printDate(long unix) {

        return new Date(unix * 1000L).toString();
    }

    /**
     * @param unix    unix
     * @param context context
     * @return 获取unix所在月份的字符串
     */
    public static String unix2Month(long unix, Context context) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unix * 1000L);
        String result = null;
        switch (c.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                result = context.getResources().getString(R.string.month1);
                break;
            case Calendar.FEBRUARY:
                result = context.getResources().getString(R.string.month2);
                break;
            case Calendar.MARCH:
                result = context.getResources().getString(R.string.month3);
                break;
            case Calendar.APRIL:
                result = context.getResources().getString(R.string.month4);
                break;
            case Calendar.MAY:
                result = context.getResources().getString(R.string.month5);
                break;
            case Calendar.JUNE:
                result = context.getResources().getString(R.string.month6);
                break;
            case Calendar.JULY:
                result = context.getResources().getString(R.string.month7);
                break;
            case Calendar.AUGUST:
                result = context.getResources().getString(R.string.month8);
                break;
            case Calendar.SEPTEMBER:
                result = context.getResources().getString(R.string.month9);
                break;
            case Calendar.OCTOBER:
                result = context.getResources().getString(R.string.month10);
                break;
            case Calendar.NOVEMBER:
                result = context.getResources().getString(R.string.month11);
                break;
            case Calendar.DECEMBER:
                result = context.getResources().getString(R.string.month12);
                break;
        }
        return result;
    }

    /**
     * @param unix    unix
     * @param context context
     * @return 转换成格式为:  xxxx年xx月 (暂时不做其它语言的格式)
     */
    public static String formatTime0(long unix, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unix * 1000L);
        int year = calendar.get(Calendar.YEAR);
        String year0 = context.getResources().getString(R.string.year);
        String month = unix2Month(unix, context);
        return year + year0 + month;
    }
}
