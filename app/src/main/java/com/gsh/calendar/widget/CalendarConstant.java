package com.gsh.calendar.widget;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;

/**
 * Created by damon on 1/8/16.
 */
public class CalendarConstant {

    /**
     * 日历(月视图)的开始时间点: 2010/01/01 00:00    ==1262275200
     */
    public static final int CALENDAR_START_MONTH = 1262275200;

    /**
     * 日历(周视图)的开始时间点: 2009/12/27 00:00    ==1261843200
     */
    public static final int CALENDAR_START_WEEK = 1261843200;

    //测试 2015/10/01 00:00
//    public static final int CALENDAR_START = 1443628800;

    /**
     * TOTAL_COL: 7列
     **/
    public static final int TOTAL_COL = Calendar.SATURDAY;

    /**
     * TOTAL_ROW_MONTH: 6行
     **/
    public static final int TOTAL_ROW_MONTH = 6;
    /**
     * 周视图行数
     */
    public static final int TOTAL_ROW_WEEK = 1;


    /**
     * 安全期
     */
    public static final int DAY_TYPE_SAFE = 100;
    /**
     * 月经开始
     */
    public static final int DAY_TYPE_MENS_START = 101;

    /**
     * 月经进行中...
     */
    public static final int DAY_TYPE_MENS_ING = 102;
    /**
     * 月经结束
     */
    public static final int DAY_TYPE_MENS_END = 103;

    /**
     * 月经只有一天
     */
    public static final int DAY_TYPE_MENS_ONLYONE = 104;

    /**
     * 易孕开始
     */
    public static final int DAY_TYPE_EASY_START = 105;

    /**
     * 易孕中的非排卵期....
     */
    public static final int DAY_TYPE_EASY_ING = 106;

    /**
     * 易孕结束
     */
    public static final int DAY_TYPE_EASY_END = 107;

    /**
     * 排卵期
     */
    public static final int DAY_TYPE_OVULATE = 108;

    /**
     * 易孕期只有一天(且不是排卵日)
     */
    public static final int DAY_TYPE_EASY_ONLYONE0 = 109;
    /**
     * 易孕期只有一天(且是排卵日)
     */
    public static final int DAY_TYPE_EASY_ONLYONE1 = 110;

    @IntDef({DAY_TYPE_SAFE, DAY_TYPE_MENS_START, DAY_TYPE_MENS_ING, DAY_TYPE_MENS_END, DAY_TYPE_MENS_ONLYONE, DAY_TYPE_EASY_START, DAY_TYPE_EASY_ING, DAY_TYPE_EASY_END, DAY_TYPE_OVULATE, DAY_TYPE_EASY_ONLYONE0, DAY_TYPE_EASY_ONLYONE1})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DayType {
    }

    /**
     * "日"上方文字 "今天"
     */
    public static final int TOP_TYPE_TODAY = 0;
    /**
     * "日"上方文字 月份 比如:"3月"
     */
    public static final int TOP_TYPE_FIRST_DAY = 1;
    /**
     * "日"上方文字 没有
     */
    public static final int TOP_TYPE_NONE = -1;

    @IntDef({TOP_TYPE_TODAY, TOP_TYPE_FIRST_DAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TopType {
    }

    /**
     * 日历 当前页有效
     */
    public static final int PAGER_VALID = 0;
    /**
     * 日历 当前页无效, 左边越界了
     */
    public static final int PAGER_INVALID_LEFT = 1;
    /**
     * 日历 当前页无效, 右边越界了
     */
    public static final int PAGER_INVALID_RIGHT = 2;

    @IntDef({PAGER_VALID, PAGER_INVALID_LEFT, PAGER_INVALID_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PagerValidState {
    }

    /**
     * 月份枚举
     */
    @IntDef({Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Month {
    }

    /**
     * 星期枚举
     */
    @IntDef({Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Week {
    }

    /**
     * cell的背景颜色的top和Bottom的偏移
     */
    public static final float BACKGROUND_DELTA0 = 0.2F;
    /**
     * cell 绘制圆弧相对cell背景颜色圆心的偏移
     */
    public static final float BACKGROUND_DELTA1 = 0.1F;


    /**
     * 左侧圆弧的起始位置
     */
    public static final float START_ANGLE_LEFT = 90F;
    /**
     * 右侧圆弧的起始位置
     */
    public static final float START_ANGLE_RIGHT = 270F;
    /**
     * 圆弧sweep的度数
     */
    public static final float SWEEP_ANGLE = 180F;
    /**
     * 绘制事件的圆点
     */
    public static final float EVENT_SIGN_RADIUS = 8F;

    /**
     * 该数组正好和日历中的显示一样的
     * 注意: 列对应的Calendar.SUNDAY--Calendar.SATURDAY(第一列没有用)
     */
    public final static int[][] CELL_ARRAY_MONTH = new int[][]{
            {0, 1, 2, 3, 4, 5, 6, 7},
            {0, 8, 9, 10, 11, 12, 13, 14},
            {0, 15, 16, 17, 18, 19, 20, 21},
            {0, 22, 23, 24, 25, 26, 27, 28},
            {0, 29, 30, 31, 32, 33, 34, 35},
            {0, 36, 37, 38, 39, 40, 41, 42}
    };
    /**
     * 该数组正好和日历中的显示一样的
     * 注意: 列对应的Calendar.SUNDAY--Calendar.SATURDAY(第一列没有用)
     */
    public final static int[][] CELL_ARRAY_WEEK = new int[][]{
            {0, 1, 2, 3, 4, 5, 6, 7}
    };

    /**
     * 通过位置获得行数
     *
     * @param position 位置 比如:28;
     * @return
     */
    public static int getRowFromPosition(int position, boolean isMonth) {
        int row = 0;
        if (isMonth) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 8; j++) {
                    if (CELL_ARRAY_MONTH[i][j] == position) {
                        return i;
                    }
                }
            }
        } else {
            return 0;
        }
        return row;
    }
}
