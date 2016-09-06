package com.gsh.calendar.data;

import com.gsh.calendar.util.DateUtil;
import com.gsh.calendar.widget.CalendarConstant;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by damon on 1/8/16.
 */
public class TestData {
    public static final String TAG = "TestData";

    /**
     * 获取一周的数据
     *
     * @param selectedUnix 该天所在的周,并且是选中的天(必须对准00:00的unix)
     * @param startUnix    时间范围的 起始天的unix
     * @param endUnix      时间范围的 结束天的unix
     * @return
     */
    public static CalendarOneScreenDataWeek generateOneScreenDataWeek(long selectedUnix, long startUnix, long endUnix) {

        CalendarOneScreenDataWeek data = new CalendarOneScreenDataWeek();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startUnix * 1000L);
        long unixStart = DateUtil.getTimeUnix(c.getTimeInMillis());

        c.setTimeInMillis(endUnix * 1000L);
        long unixEnd = DateUtil.getTimeUnix(c.getTimeInMillis());
//        Log.d(TAG, "generateOneScreenDataWeek-->>unixStart-->>" + new Date(unixStart * 1000L) + ", unixEnd-->>" + new Date(unixEnd * 1000L) + ", selectedUnix-->>" + new Date(selectedUnix * 1000L));

        c.setTimeInMillis(selectedUnix * 1000L);
        //默认选中
        int defaultWeek = c.get(Calendar.DAY_OF_WEEK);

        data.setWeekOfYear(c.get(Calendar.WEEK_OF_YEAR));
        data.setYear(c.get(Calendar.YEAR));

        long today = DateUtil.getTodayUnix();

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        c.add(Calendar.DATE, -(dayOfWeek - 1));
        getValidStateWeek(data, (int) (c.getTimeInMillis() / 1000L));

        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayCurrentIndex = c.get(Calendar.DAY_OF_MONTH);
            long unix = DateUtil.getTimeUnix(c.getTimeInMillis());
            DayData dayData = new DayData();
            dayData.setYear(year);
            dayData.setMonth(month);
            dayData.setClickable(unix >= unixStart && unix <= unixEnd);
//            Log.d(TAG, "generateOneScreenDataWeek-->>unix-->>" + new Date(unix*1000L));

            setDayType(dayData, dayCurrentIndex);

            long unixCurrent = DateUtil.getTimeUnix(year, month, dayCurrentIndex);
            if (dayCurrentIndex == 1) {
                dayData.setTopType(CalendarConstant.TOP_TYPE_FIRST_DAY);
            } else if (today == unixCurrent) {
                dayData.setTopType(CalendarConstant.TOP_TYPE_TODAY);
            } else {
                dayData.setTopType(CalendarConstant.TOP_TYPE_NONE);

            }
            if (i == defaultWeek) {
                //默认选中第一天
                data.setSelectedPosition(CalendarConstant.CELL_ARRAY_MONTH[0][i]);
            }
            setEvent(dayData, dayCurrentIndex);
            dayData.setUnix(unixCurrent);
            dayData.setDay(dayCurrentIndex);
            c.add(Calendar.DATE, 1);

            data.getMapDay().put(CalendarConstant.CELL_ARRAY_MONTH[0][i], dayData);
        }

        return data;
    }


    /**
     * 生成一屏幕的数据
     *
     * @param year  年
     * @param month 月
     * @return 生成一屏幕的数据
     */
    public static CalendarOneScreenDataMonth generateOneScreenDataMonth(int year, int month, int selectedDay) {
//        Log.d(TAG,"generateOneScreenDataMonth-->>year-->>"+year+", month-->>"+month+", selectedDay-->>"+selectedDay);
        CalendarOneScreenDataMonth data = new CalendarOneScreenDataMonth();
        data.setYear(year);
        data.setMonth(month);
        Map<Integer, DayData> mapDay = data.getMapDay();

        getValidStateMonth(data, year, month, selectedDay);

        int lastMonth;
        int lastYear;
        int nextMonth;
        int nextYear;

        if (month == Calendar.JANUARY) {
            lastMonth = Calendar.DECEMBER;
            lastYear = year - 1;
        } else {
            lastMonth = month - 1;
            lastYear = year;
        }
        if (month == Calendar.DECEMBER) {
            nextMonth = Calendar.JANUARY;
            nextYear = year + 1;
        } else {
            nextMonth = month + 1;
            nextYear = year;
        }


        int lastMonthDays = DateUtil.getMonthDays(lastYear, lastMonth); // 上个月的天数
        int currentMonthDays = DateUtil.getMonthDays(year, month); // 当前月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(year, month, 1);// 本月的第一天是星期几?
        int lastDayWeek = DateUtil.getWeekDayFromDate(year, month, currentMonthDays);// 本月的最后一天是星期几?
//        Log.d("test", "lastMonthDays-->>" + lastMonthDays + ", currentMonthDays-->>" + currentMonthDays + ", firstDayWeek-->>" + firstDayWeek + ", lastDayWeek-->>" + lastDayWeek);
        //下个月天数的index
        int dayNextIndex = 1;
        //当月天数的index
        int dayCurrentIndex = 1;

        long today = DateUtil.getTodayUnix();


        //上个月和这个月所占的格数
        int countT = currentMonthDays + firstDayWeek - 1;
        //下个月起始的行数
        int nextMonthRow = countT % Calendar.SATURDAY == 0 ? countT / Calendar.SATURDAY - 1 : countT / Calendar.SATURDAY;
        for (int j = 0; j < CalendarConstant.TOTAL_ROW_MONTH; j++) {
            for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
                DayData dayData = new DayData();
                dayData.setWeek(i);
                if (j == 0 && i < firstDayWeek) {
                    //设置上个月的的数据
                    dayData.setYear(lastYear);
                    dayData.setMonth(lastMonth);
                    int dayLastIndex = lastMonthDays - (firstDayWeek - 1) + i;
                    dayData.setDay(dayLastIndex);
                    dayData.setDayType(CalendarConstant.DAY_TYPE_SAFE);
                    dayData.setHasCircleBottom(false);
                    dayData.setTopType(CalendarConstant.TOP_TYPE_NONE);
                    dayData.setUnix(DateUtil.getTimeUnix(lastYear, lastMonth, dayLastIndex));
//                    Log.d(TAG,"上个月-->>"+DateUtil.getTimeUnix(lastYear, lastMonth, dayLastIndex));
//                    Log.d(TAG,"上个月-->>lastYear-->>"+lastYear+", lastMonth-->>"+lastMonth+", dayLastIndex-->>"+dayLastIndex);
                    dayData.setClickable(false);
                } else if (j > nextMonthRow || (j == nextMonthRow && i > lastDayWeek)) {
                    //设置下个月的数据
                    dayData.setYear(nextYear);
                    dayData.setMonth(nextMonth);
                    dayData.setDayType(CalendarConstant.DAY_TYPE_SAFE);
                    dayData.setHasCircleBottom(false);
                    long unixNext = DateUtil.getTimeUnix(nextYear, nextMonth, dayNextIndex);
//                    Log.d(TAG,"下个月-->>"+unixNext);
                    dayData.setTopType(dayNextIndex == 1 ? CalendarConstant.TOP_TYPE_FIRST_DAY : CalendarConstant.TOP_TYPE_NONE);
                    dayData.setUnix(unixNext);
                    dayData.setClickable(false);
                    dayData.setDay(dayNextIndex++);
                } else {
                    //设置当月的数据
                    dayData.setYear(year);
                    dayData.setMonth(month);
                    setDayType(dayData, dayCurrentIndex);

//                    dayData.setDayType(CalendarConstant.DAY_TYPE_SAFE);
                    long unixCurrent = DateUtil.getTimeUnix(year, month, dayCurrentIndex);


                    if (dayCurrentIndex == 1) {
                        dayData.setTopType(CalendarConstant.TOP_TYPE_FIRST_DAY);
                    } else if (today == unixCurrent) {
                        dayData.setTopType(CalendarConstant.TOP_TYPE_TODAY);
                    } else {
                        dayData.setTopType(CalendarConstant.TOP_TYPE_NONE);

                    }
                    if (selectedDay >= 1 && selectedDay <= currentMonthDays) {
                        if (selectedDay == dayCurrentIndex) {
                            data.setSelectedPosition(CalendarConstant.CELL_ARRAY_MONTH[j][i]);
                        }
                    } else {
                        if (selectedDay == (currentMonthDays - 1)) {
                            //默认选中最后一天
                            data.setSelectedPosition(CalendarConstant.CELL_ARRAY_MONTH[j][i]);
                        }
                    }
                    setEvent(dayData, dayCurrentIndex);
//                    Log.d(TAG,"当前月-->>"+unixCurrent);
                    dayData.setUnix(unixCurrent);
                    dayData.setDay(dayCurrentIndex++);
                    dayData.setClickable(true);
                }
                mapDay.put(CalendarConstant.CELL_ARRAY_MONTH[j][i], dayData);
            }
        }
        return data;

    }

    private static void getValidStateMonth(CalendarOneScreenDataMonth data, int year, int month, int selectedDay) {

        Calendar c0 = Calendar.getInstance();
        c0.set(year, month, selectedDay);
        //判断其实日期是否有效
        boolean startValid = (c0.getTimeInMillis() / 1000L) >= CalendarConstant.CALENDAR_START_WEEK;
        //Damontodo 结束范围为填写真实数据时实现.
//       2016/07/01 00:00 1467302400
        boolean endValid = (c0.getTimeInMillis() / 1000L) <= 1467302400;
        if (startValid && endValid) {
            data.setInvalidState(CalendarConstant.PAGER_VALID);
        } else if (!startValid) {
            data.setInvalidState(CalendarConstant.PAGER_INVALID_LEFT);
        } else {
            data.setInvalidState(CalendarConstant.PAGER_INVALID_RIGHT);
        }
    }

    private static void getValidStateWeek(CalendarOneScreenDataWeek data, int unix) {

        Calendar c0 = Calendar.getInstance();
        c0.setTimeInMillis(unix * 1000L);
        //判断其实日期是否有效
        boolean startValid = (c0.getTimeInMillis() / 1000L) >= CalendarConstant.CALENDAR_START_MONTH;
        //Damontodo 结束范围为填写真实数据时实现.
        // 周视图结束的范围为: 预测月经期所在月的最后一周
//       2016/07/01 00:00 1467302400
        boolean endValid = (c0.getTimeInMillis() / 1000L) <= 1467302400;
        if (startValid && endValid) {
            data.setInvalidState(CalendarConstant.PAGER_VALID);
        } else if (!startValid) {
            data.setInvalidState(CalendarConstant.PAGER_INVALID_LEFT);
        } else {
            data.setInvalidState(CalendarConstant.PAGER_INVALID_RIGHT);
        }
    }


    private static void setEvent(DayData dayData, int dayNextIndex) {
        if (dayNextIndex > 5 && dayNextIndex < 20) {
            dayData.setHasCircleBottom(true);
        } else {
            dayData.setHasCircleBottom(false);

        }
    }

    private static void setDayType(DayData dayData, int dayNextIndex) {
        if (dayNextIndex >= 9 && dayNextIndex <= 13) {
            if (dayNextIndex == 9) {
                dayData.setDayType(CalendarConstant.DAY_TYPE_MENS_START);
            } else if (dayNextIndex == 13) {
                dayData.setDayType(CalendarConstant.DAY_TYPE_MENS_END);
            } else {
                dayData.setDayType(CalendarConstant.DAY_TYPE_MENS_ING);
            }
        } else if (dayNextIndex == 1) {
            dayData.setDayType(CalendarConstant.DAY_TYPE_MENS_ONLYONE);
        } else if (dayNextIndex == 18) {
            dayData.setDayType(CalendarConstant.DAY_TYPE_EASY_ONLYONE0);
        } else if (dayNextIndex == 20) {
            dayData.setDayType(CalendarConstant.DAY_TYPE_EASY_ONLYONE1);
        } else if (dayNextIndex >= 26 && dayNextIndex <= 30) {
            if (dayNextIndex == 26) {
                dayData.setDayType(CalendarConstant.DAY_TYPE_EASY_START);
            } else if (dayNextIndex == 30) {
                dayData.setDayType(CalendarConstant.DAY_TYPE_EASY_END);
            } else if (dayNextIndex == 28) {
//                            排卵日
                dayData.setDayType(CalendarConstant.DAY_TYPE_OVULATE);
            } else {
                dayData.setDayType(CalendarConstant.DAY_TYPE_EASY_ING);

            }

        } else {

            dayData.setDayType(CalendarConstant.DAY_TYPE_SAFE);
        }
    }

}
