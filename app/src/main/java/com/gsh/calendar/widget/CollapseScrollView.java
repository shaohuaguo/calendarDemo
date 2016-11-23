package com.gsh.calendar.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;


import com.gsh.calendar.MainActivity;
import com.gsh.calendar.MensColorDetailActivity;
import com.gsh.calendar.R;
import com.gsh.calendar.adapter.CalendarAdapter;
import com.gsh.calendar.data.CalendarOneScreenDataMonth;
import com.gsh.calendar.data.CalendarOneScreenDataWeek;
import com.gsh.calendar.data.DayData;
import com.gsh.calendar.data.TestData;
import com.gsh.calendar.util.DateUtil;
import com.gsh.calendar.util.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by damon on 1/5/16.
 * 可以折叠的滚动视图
 */
public class CollapseScrollView extends FrameLayout implements View.OnClickListener {


    private final String TAG = "CollapseScrollView";
//    /**
//     * viewPager切换Pager模式儿 非触摸滑动
//     */
//    private static final int PAGER_SELECTED_MODE_0 = 0;
//
//    /**
//     * viewPager切换Pager模式儿 触摸滑动
//     */
//    private static final int PAGER_SELECTED_MODE_1 = 1;
//
//
//    @IntDef({PAGER_SELECTED_MODE_0, PAGER_SELECTED_MODE_1})
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface PagerSelectedMode {
//    }
//
//    /**
//     * viewPager切换Pager模式儿
//     */
//    @PagerSelectedMode
//    public int pagerSelectedMode = PAGER_SELECTED_MODE_1;

    /**
     * viewPager 月视图模式
     */
    private static final int PAGER_MODE_MONTH = 0;

    /**
     * viewPager 周视图模式
     */
    private static final int PAGER_MODE_WEEK = 1;


    @IntDef({PAGER_MODE_MONTH, PAGER_MODE_WEEK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PagerMode {
    }

    /**
     * viewPager切换Pager模式儿
     */
    @PagerMode
    public int pagerMode = PAGER_MODE_MONTH;


    /**
     * 指示器布局隐藏时遗留的高度 10(单位dp)
     */
    private static final int INDICATOR_LAYOUT_OFFSET_DP = 8;
    /**
     * 指示器布局隐藏时遗留的高度 (单位px)
     */
    private int indicatorLayoutOffsetPx;


    /**
     * 月视图 View集合
     */
    @NonNull
    public CalendarCard[] viewsMonth;
    /**
     * 周视图 View集合
     */
    @NonNull
    public CalendarCard[] viewsWeek;


    /**
     * 外层可滚动视图
     */
    private LinearLayout scrollLayout;

    /**
     * ViewPager的不折叠的顶部的位置
     */
    private int unFoldingTop = 0;
    /**
     * ViewPager的不折叠的的位置
     */
    private int unFoldingBottom = 0;

    /**
     * 日历每一行的高度
     */
    private int calendarRowHeight = 0;
    /**
     * 日历的ViewPager 月视图
     */
    public CalendarViewPager calendarViewPagerMonth;
    /**
     * 日历的ViewPager 周视图
     */
    public CalendarViewPager calendarViewPagerWeek;

    /**
     * 日历的高度
     */
    private int calendarViewPagerHeight;


    /**
     * 颜色指示器布局
     */
    public RelativeLayout colorIndicatorLayout;

    /**
     * 颜色指示器布局的高度
     * 1.它的高度是不变的,要么隐藏,要么显示
     */
    private int colorIndicatorLayoutHeight;

    /**
     * 描述内容布局(颜色指示器下方)
     */
    public InnerScrollView descriptionLayout;

    /**
     * 描述内容布局的高度
     */
    private int descriptionLayoutHeight;

    /**
     * 该控件初始的高度
     */
    private int height;

    //    上次触摸的位置
    private float mMotionLastY;


    /**
     * 上次动画结束的位置(初始值为0)
     */
    private int lastAnimationEndY = 0;

    /**
     * action_move的阀值
     */
    private float mSlop;

    /**
     * 处理up事件的Scroller,线性的Scroller
     */
    private Scroller upScroller;

    /**
     * 0-->>unFoldingTop的Runnable
     */
    private Runnable scrollRunnable0;

    /**
     * unFoldingTop-->>unFoldingBottom的 ScrollView Scroller
     */
    private Scroller upScroller2;
    /**
     * unFoldingTop-->>unFoldingBottom的 ScrollView Runnable
     */
    private Runnable scrollRunnable2;

    /**
     * 月视图左边的边界的日期:unix 默认为:0(实际的的边界数据)
     */
    public long viewPagerRealLeft = 0;
    /**
     * 月视图右边的边界的日期:unix 默认为:Integer.MAX_VALUE(实际的边界数据)
     */
    public long viewPagerRealRight = Integer.MAX_VALUE;


    /**
     * 指示器打开关闭的Scroller
     */
    private Scroller indicatorScroller;

    /**
     * 指示器打开的Runnable
     */
    private Runnable indicatorOpenRunnable;
    /**
     * 指示器关闭的Runnable
     */
    private Runnable indicatorCloseRunnable;

    /**
     * 默认的动画时间
     */
    private static final int DURATION_DEFAULT = 300;
    /**
     * 动画时间计算的系数
     */
    private static final int DURATION_FACTOR = 5;

    /**
     * 指示器布局的动画时间
     */
    private static final int DURATION_COLOR_LAYOUT = 300;

    /**
     * 当前月视图的在视图集合中index
     */
    public int mCurrentIndexMonth = 0;
    /**
     * 当前周视图的在视图集合中index
     */
    public int mCurrentIndexWeek = 0;

    /**
     * 指示器打开状态
     */
    private final static int INDICATOR_OPEN = 0;
    /**
     * 指示器关闭状态
     */
    private final static int INDICATOR_CLOSE = 1;
    /**
     * 指示器打开中...
     */
    private final static int INDICATOR_OPENING = 2;
    /**
     * 指示器关闭中...
     */
    private final static int INDICATOR_CLOSEING = 3;
    /**
     * 指示器布局当前的状态
     */
    private int indicatorLayoutState = INDICATOR_OPEN;

    /**
     * 月视图Pager的childCount
     */
    public int childCountMonth;
    /**
     * 周视图Pager的childCount
     */
    public int childCountWeek;

    /**
     * true:初始化周视图数据
     */
    private boolean isInitWeekData = true;
    /**
     * true:初始化月视图数据
     */
    private boolean isInitMonthData = true;

    public CollapseScrollView(Context context) {
        super(context);
        init();
    }

    public CollapseScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CollapseScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        indicatorLayoutOffsetPx = Utils.dipValue2PxValue(getContext(), INDICATOR_LAYOUT_OFFSET_DP);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (calendarRowHeight == 0) {
                    //初始化高度
                    calendarViewPagerHeight = calendarViewPagerMonth.getHeight();
                    colorIndicatorLayoutHeight = colorIndicatorLayout.getHeight();
                    descriptionLayoutHeight = descriptionLayout.getHeight();
                    height = getHeight();
                    Log.d(TAG, "calendarViewPagerHeight-->>" + calendarViewPagerHeight);
                    Log.d(TAG, "colorIndicatorLayoutHeight-->>" + colorIndicatorLayoutHeight);
                    Log.d(TAG, "descriptionLayoutHeight-->>" + descriptionLayoutHeight);
                    Log.d(TAG, "scrollViewHeight-->>" + height);
                    calendarRowHeight = calendarViewPagerHeight / CalendarConstant.TOTAL_ROW_MONTH + 1;
                    int row = CalendarConstant.getRowFromPosition(viewsMonth[mCurrentIndexMonth].dataMonth.getSelectedPosition(), true);
                    unFoldingTop = calendarRowHeight * row;
                    unFoldingBottom = calendarRowHeight * (CalendarConstant.TOTAL_ROW_MONTH - 1);
                    Log.d(TAG, "unFoldingTop-->>" + unFoldingTop + ", unFoldingBottom-->>" + unFoldingBottom + ", row-->>" + row);
                }
            }
        });
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        upScroller = new Scroller(getContext(), new LinearInterpolator());
        upScroller2 = new Scroller(getContext(), new LinearInterpolator());
        indicatorScroller = new Scroller(getContext(), new LinearInterpolator());

        initRunnable();
    }


    /**
     *
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "设置测试数据-->>onFinishInflate");
        CollapseScrollView rootView = (CollapseScrollView) findViewById(R.id.collapse_scrollview_layout);
        scrollLayout = (LinearLayout) rootView.findViewById(R.id.scroll_layout);
        calendarViewPagerMonth = (CalendarViewPager) rootView.findViewById(R.id.collapse_scrollview_viewpager_month);
        calendarViewPagerWeek = (CalendarViewPager) rootView.findViewById(R.id.collapse_scrollview_viewpager_week);
        colorIndicatorLayout = (RelativeLayout) rootView.findViewById(R.id.collapse_scrollview_color_indicator);
        descriptionLayout = (InnerScrollView) rootView.findViewById(R.id.collapse_scrollview_description);
        rootView.findViewById(R.id.collapse_scrollview_color_detail).setOnClickListener(this);

        //设置布局参数
        ViewGroup.LayoutParams params = calendarViewPagerMonth.getLayoutParams();
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        params.height = (screenWidth / CalendarConstant.TOTAL_COL + 1) * CalendarConstant.TOTAL_ROW_MONTH;
        params.width = screenWidth;
        calendarViewPagerMonth.setLayoutParams(params);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(screenWidth, screenWidth / CalendarConstant.TOTAL_COL + 1);
        calendarViewPagerWeek.setLayoutParams(params2);
        setPagerData();
    }

    /**
     * 设置Pager的数据
     */
    private void setPagerData() {
        //        setTestData();
        calendarViewPagerWeek.setAdapter(getAdapterWeek());
        calendarViewPagerWeek.setOffscreenPageLimit(1);//左右各只有一张是空闲的.
        calendarViewPagerWeek.setVisibility(View.INVISIBLE);
        calendarViewPagerWeek.setOnPageChangeListener(new PagerScrollListenerWeek());
        isInitWeekData = false;
        calendarViewPagerWeek.setCurrentItem(mCurrentIndexWeek);

        calendarViewPagerMonth.setAdapter(getAdapterMonth());
        isInitMonthData = true;//第一次没有调用
        calendarViewPagerMonth.setCurrentItem(mCurrentIndexMonth);
        calendarViewPagerMonth.setOffscreenPageLimit(1);//左右各只有一张是空闲的.
        calendarViewPagerMonth.addOnPageChangeListener(new PagerScrollListenerMonth());
    }

    /**
     * 设置descriptionLayout的params
     */
    private void setContentParams() {

        ViewGroup.LayoutParams params = descriptionLayout.getLayoutParams();
        params.height = descriptionLayoutHeight + colorIndicatorLayoutHeight + scrollLayout.getScrollY();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        descriptionLayout.setLayoutParams(params);

    }

    /**
     * 设置上次ViewPager停留的位置
     */
    private void setMotionLastY() {
        int currentScrollY = scrollLayout.getScrollY();
//        Log.d(TAG, "currentScrollY-->>" + currentScrollY + ", unFoldingBottom-->>" + unFoldingBottom + ", unFoldingTop-->>" + unFoldingTop);
        if (currentScrollY == 0) {
            lastAnimationEndY = 0;
        } else if (currentScrollY == unFoldingBottom) {
            lastAnimationEndY = unFoldingBottom;
        }
    }

    /**
     * ACTION_UP时, 动画移动到固定的位置
     */
    private void move2Position() {
        int realYPosition;
        if (lastAnimationEndY == 0) {
            //月日历模式
            realYPosition = scrollLayout.getScrollY();
            boolean isRestore = realYPosition < calendarRowHeight;
            Log.d(TAG, "月日历模式lastAnimationEndY-->>" + lastAnimationEndY + ", isRestore-->>" + isRestore);
            if (isRestore) {
                //Damontodo 还原到月日历模式
                if (realYPosition > 0) {
                    int duration = getDuration(Math.abs(realYPosition));
                    Log.d(TAG, "还原到月日历模式ScrollView realYPosition-->>" + (realYPosition) + ", duration-->>" + duration);
                    upScroller.startScroll(0, realYPosition, 0, -realYPosition, duration);
                    post(scrollRunnable0);
                }
            } else {
                //Damontodo 切换到周日历模式
                if (realYPosition < unFoldingBottom) {
                    int realDelta = unFoldingBottom - realYPosition;
                    int duration = getDuration(Math.abs(realDelta));
                    Log.d(TAG, "切换到周日历模式ScrollView-->>startY-->>" + realYPosition + ", realDelta-->>" + realDelta + ", duration-->>" + duration);
                    //需要滚动ScrollView
                    upScroller2.startScroll(0, realYPosition, 0, realDelta, duration);
                    post(scrollRunnable2);
                }


            }
        } else if (lastAnimationEndY == unFoldingBottom) {
            //周日历模式
            realYPosition = scrollLayout.getScrollY();
            int delta = unFoldingBottom - realYPosition;
            boolean isRestore = delta < calendarRowHeight;
            Log.d(TAG, "周日历模式lastAnimationEndY-->>" + lastAnimationEndY + ", isRestore-->>" + isRestore);
            if (isRestore) {
                //Damontodo 还原到周日历模式
                if (realYPosition < unFoldingBottom) {
                    int duration = getDuration(Math.abs(delta));
                    Log.d(TAG, "还原到周日历模式ScrollView-->>startY-->>" + realYPosition + ", realDelta-->>" + delta + ", duration-->>" + duration);
                    //需要滚动ScrollView
                    upScroller2.startScroll(0, realYPosition, 0, delta, duration);
                    post(scrollRunnable2);
                }

            } else {
                //Damontodo 切换到月日历模式
                Log.d(TAG, "切换到月日历模式");
                if (realYPosition < unFoldingBottom) {
                    int realDelta = -realYPosition;
                    int duration = getDuration(Math.abs(realDelta));
                    Log.d(TAG, "切换到月日历模式ScrollView-->>startY-->>" + realYPosition + ", realDelta-->>" + realDelta + ", duration-->>" + duration);
                    //需要滚动ScrollView
                    upScroller2.startScroll(0, realYPosition, 0, realDelta, duration);
                    post(scrollRunnable2);
                }

            }
        }

    }

    /**
     * 获取动画的时间, 这个算法不好,需要看下有没有更好的算法
     *
     * @param distance 滚动向量
     * @return
     */
    private int getDuration(int distance) {
        int duration = DURATION_FACTOR * (distance);
        return duration > DURATION_DEFAULT ? DURATION_DEFAULT : duration;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "dispatchTouchEvent-->>MotionEvent-->>"+Utils.actionToString(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
//        Log.d(TAG, "onInterceptTouchEvent-->>MotionEvent-->>" + Utils.actionToString(action) + ", scrollLayout.getScrollY()-->>" + scrollLayout.getScrollY());
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                descriptionLayout.isIntercept = false;
                mMotionLastY = y;
                setMotionLastY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (updateIsIntercept(ev)) {
                    return super.onInterceptTouchEvent(ev);
                }
                if (Math.abs(mMotionLastY - y) > mSlop && !descriptionLayout.isIntercept) {

//                    Log.d(TAG, "onInterceptTouchEvent-->>拦截事件");
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        float y = ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mMotionLastY = y;
            setMotionLastY();
            return true;
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float distance = mMotionLastY - y;
            int roundedDistance = Math.round(distance);
            touchScroll(roundedDistance);
            setContentParams();
            mMotionLastY = y;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //Damontodo 需要执行动画,回到相应的位置
            move2Position();
//            Log.d(TAG, "description...height-->>" + descriptionLayout.getLayoutParams().height);
//            Log.d(TAG, "scrollView...height-->>" + getLayoutParams().height);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 处理是否需要拦截touch事件
     *
     * @param ev MotionEvent
     * @return true:拦截 touch事件
     */
    private boolean updateIsIntercept(MotionEvent ev) {
        if (scrollLayout.getScrollY() != unFoldingBottom) {
            return false;
        }
        float distance = mMotionLastY - ev.getY();
        if (descriptionLayout.getScrollY() == 0 && distance > 0) {
//            Log.d(TAG, "0交给descriptionLayout");
            descriptionLayout.isIntercept = true;
        } else if (descriptionLayout.getScrollY() != 0) {
//            Log.d(TAG, "1交给descriptionLayout");
            descriptionLayout.isIntercept = true;
        }
//        Log.d(TAG, "descriptionLayout.getScrollY()-->>" + descriptionLayout.getScrollY() + ",distance-->>" + distance + ", descriptionLayout.isIntercept-->>" + descriptionLayout.isIntercept);
        return descriptionLayout.isIntercept;
    }

    /**
     * 处理触摸滚动操作
     *
     * @param distance 滚动向量
     */
    private void touchScroll(int distance) {

        if (unFoldingTop == 0) {
            //当位于第一行时
//            calendarViewPagerWeek.setVisibility(scrollLayout.getScrollY() > 0 ? View.VISIBLE : INVISIBLE);
            setWeekMode(scrollLayout.getScrollY() > 0);
            if (scrollLayout.getScrollY() < 0) {
                scrollLayout.scrollTo(0, 0);
            } else if (scrollLayout.getScrollY() == 0 && distance < 0) {
                return;
            } else if (unFoldingBottom >= scrollLayout.getScrollY()) {
                overBottom(distance);
            }
        } else {
            //非第一行时
            if (unFoldingTop > scrollLayout.getScrollY()) {
                lessTop(distance);
            } else if (unFoldingBottom >= scrollLayout.getScrollY()) {
                overBottom(distance);
            }
        }
    }

    /**
     * 触摸滚动,getScrollY()小于unFoldingTop时
     *
     * @param distance 滚动向量
     */
    private void lessTop(int distance) {
//                    Log.d(TAG, "不需要折叠-->>distance-->>" + distance + " scrollLayout.getScrollY-->>" + scrollLayout.getScrollY() + ", viewPager.ScrollY-->>" + calendarViewPagerMonth.getScrollY());
//        calendarViewPagerWeek.setVisibility(View.INVISIBLE);
        setWeekMode(false);
        if (distance > 0) {
            if (scrollLayout.getScrollY() < unFoldingTop && scrollLayout.getScrollY() + distance > unFoldingTop) {
                //Damontodo 向上滑动到unFoldingTop临界值时
                distance = (unFoldingTop - scrollLayout.getScrollY());
            }
        } else {
            if (scrollLayout.getScrollY() + distance < 0) {
                //Damontodo 向下滑动,防止顶部越界滚动
                distance = -scrollLayout.getScrollY();
            }
        }
        scrollLayout.scrollBy(0, distance);

    }

    /**
     * 触摸滚动,getScrollY()大于unFoldingBottom时
     *
     * @param distance 向量
     */
    private void overBottom(int distance) {
//                    Log.d(TAG, "需要折叠0-->>distance-->>" + distance + " scrollLayout.getScrollY-->>" + scrollLayout.getScrollY() + ", viewPager.ScrollY-->>" + calendarViewPagerMonth.getScrollY());
//        calendarViewPagerWeek.setVisibility(View.VISIBLE);
        setWeekMode(true);
        if (distance < 0) {
            //Damontodo 这里不需要做越界的滚动处理,因为没有任何影响.
        } else {
            if (scrollLayout.getScrollY() + distance > unFoldingBottom) {
                //Damontodo 向上滑动到unFoldingBottom临界值时
                distance = unFoldingBottom - scrollLayout.getScrollY();
            }
        }
        scrollLayout.scrollBy(0, distance);
        if (scrollLayout.getScrollY() == unFoldingBottom) {
            //内容滚动模式
//            Log.d(TAG, "传递给内容视图-->>distance-->>" + distance + ", scrollLayout.getScrollY-->>" + scrollLayout.getScrollY() + ", descriptionLayout.getScrollY-->>" + descriptionLayout.getScrollY());
        }
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

    private CalendarAdapter getAdapterMonth() {
        //计算出viewPager的childCount;
        long selectedDay = DateUtil.getTodayUnix();
        childCountMonth = DateUtil.getPagerMonth(viewPagerRealLeft, viewPagerRealRight);

        viewsMonth = new CalendarCard[childCountMonth];
        mCurrentIndexMonth = DateUtil.getSelectedPagerMonthIndex(viewPagerRealLeft, selectedDay);
        initViewsMonth(selectedDay, true);
        CalendarAdapter adapter = new CalendarAdapter<>(viewsMonth);
        adapter.setType("month");
        return adapter;

    }


    private CalendarAdapter getAdapterWeek() {
        long selectedDay = DateUtil.getTodayUnix();
        childCountWeek = DateUtil.getPagerWeek(viewPagerRealLeft, viewPagerRealRight);
        viewsWeek = new CalendarCard[childCountWeek];

        mCurrentIndexWeek = DateUtil.getSelectedPagerWeekIndex(viewPagerRealLeft, selectedDay);
        initViewsWeek(selectedDay, true);
        CalendarAdapter calendarAdapterWeek = new CalendarAdapter<>(viewsWeek);
        calendarAdapterWeek.setType("week");
        return calendarAdapterWeek;
    }

    /**
     * @param unix            中间的Item&&选中的天
     * @param isUpdateCurrent true:当前index的视图数据也更新 false:则不更新
     */
    private void initViewsMonth(long unix, boolean isUpdateCurrent) {
        int realPositionCurr = mCurrentIndexMonth;
        int realPositionPre = mCurrentIndexMonth - 1;
        int realPositionNext = mCurrentIndexMonth + 1;
//        Log.d(TAG, "initViewsMonth-->>mCurrentIndexMonth-->>" + mCurrentIndexMonth + ", unix-->>" + unix + ", isUpdateCurrent-->>" + isUpdateCurrent);
        if (realPositionPre >= 0) {
            Calendar calendarPre = Calendar.getInstance();
            calendarPre.setTimeInMillis(unix * 1000L);
            calendarPre.add(Calendar.MONTH, -1);
            int dayPre = calendarPre.get(Calendar.DAY_OF_MONTH);
            CalendarOneScreenDataMonth dataPre = TestData.generateOneScreenDataMonth(calendarPre.get(Calendar.YEAR), calendarPre.get(Calendar.MONTH), dayPre);
            if (viewsMonth[realPositionPre] == null) {
                CalendarCard view = new CalendarCard(getContext(), dataPre);
                viewsMonth[realPositionPre] = view;
                viewsMonth[realPositionPre].setOnCellClickListener(new CellClickListenerMonth());
            } else {
                viewsMonth[realPositionPre].dataMonth = dataPre;
                viewsMonth[realPositionPre].postInvalidate();
            }
        } else {
            Log.e(TAG, "month-->>pre越界了-->>" + realPositionPre);
        }
        if (isUpdateCurrent) {
            Calendar calendarCurr = Calendar.getInstance();
            calendarCurr.setTimeInMillis(unix * 1000L);
            int dayCurr = calendarCurr.get(Calendar.DAY_OF_MONTH);
            CalendarOneScreenDataMonth dataCurr = TestData.generateOneScreenDataMonth(calendarCurr.get(Calendar.YEAR), calendarCurr.get(Calendar.MONTH), dayCurr);
            if (realPositionCurr >= 0 && realPositionCurr < childCountMonth) {
                if (viewsMonth[realPositionCurr] == null) {
                    CalendarCard view = new CalendarCard(getContext(), dataCurr);
                    viewsMonth[realPositionCurr] = view;
                    viewsMonth[realPositionCurr].setOnCellClickListener(new CellClickListenerMonth());
                } else {
                    viewsMonth[realPositionCurr].dataMonth = dataCurr;
                    viewsMonth[realPositionCurr].postInvalidate();
                }
            } else {
                Log.e(TAG, "month-->>curr越界了-->>" + realPositionCurr);
            }
        }
        if (realPositionNext < childCountMonth) {
            Calendar calendarNext = Calendar.getInstance();
            calendarNext.setTimeInMillis(unix * 1000L);
            calendarNext.add(Calendar.MONTH, 1);
            int dayNext = calendarNext.get(Calendar.DAY_OF_MONTH);
            CalendarOneScreenDataMonth dataNext = TestData.generateOneScreenDataMonth(calendarNext.get(Calendar.YEAR), calendarNext.get(Calendar.MONTH), dayNext);
            if (viewsMonth[realPositionNext] == null) {
                CalendarCard view = new CalendarCard(getContext(), dataNext);
                viewsMonth[realPositionNext] = view;
                viewsMonth[realPositionNext].setOnCellClickListener(new CellClickListenerMonth());
            } else {
                viewsMonth[realPositionNext].dataMonth = dataNext;
                viewsMonth[realPositionNext].postInvalidate();
            }
        } else {
            Log.e(TAG, "month-->>next越界了-->>" + realPositionNext);
        }

    }

    /**
     * @param unix            中间的Item&&选中的天(必须对准00:00unix)
     * @param isUpdateCurrent true:更新当前的Week页面
     */
    private void initViewsWeek(long unix, boolean isUpdateCurrent) {
        int realPositionCurr = mCurrentIndexWeek;
        int realPositionPre = mCurrentIndexWeek - 1;
        int realPositionNext = mCurrentIndexWeek + 1;
//        Log.d(TAG, "initViewsWeek-->>mCurrentIndexWeek-->>" + mCurrentIndexWeek + ", unix-->>" + new Date(unix * 1000L));
        if (realPositionPre >= 0) {
            Calendar calendarPre = Calendar.getInstance();
            calendarPre.setTimeInMillis(unix * 1000L);
            calendarPre.add(Calendar.WEEK_OF_YEAR, -1);
            //期望选中的天
            int respectedTime = (int) (calendarPre.getTimeInMillis() / 1000L);
            long selectedTime = respectedTime;
            if (realPositionPre == 0) {
                //Damontodo 第一页
                if (respectedTime < viewPagerRealLeft) {
                    selectedTime = viewPagerRealLeft;
                }
                Log.w(TAG, "第一星期了0-->>" + new Date(selectedTime * 1000L));
            }


            CalendarOneScreenDataWeek dataPre = TestData.generateOneScreenDataWeek(selectedTime, viewPagerRealLeft, viewPagerRealRight);
            if (viewsWeek[realPositionPre] == null) {
                CalendarCard view = new CalendarCard(getContext(), dataPre);
                view.setId(View.generateViewId());
                viewsWeek[realPositionPre] = view;
                viewsWeek[realPositionPre].setOnCellClickListener(new CellClickListenerWeek());
//                Log.w(TAG, "week, pre,重新初始化-->>" + new Date(selectedTime * 1000L));
            } else {
                viewsWeek[realPositionPre].dataWeek = dataPre;
                viewsWeek[realPositionPre].postInvalidate();
//                Log.w(TAG, "week, pre,更新数据-->>" + new Date(selectedTime * 1000L));
            }
        } else {
            Log.e(TAG, "week, pre越界了-->>" + realPositionPre);
        }
        if (isUpdateCurrent) {
            if (mCurrentIndexWeek > 0 && mCurrentIndexWeek < childCountWeek) {
                Calendar calendarCurr = Calendar.getInstance();
                calendarCurr.setTimeInMillis(unix * 1000L);
                //期望选中的天
                int respectedTime = (int) (calendarCurr.getTimeInMillis() / 1000L);
                long selectedTime = respectedTime;
                if (mCurrentIndexWeek == 0) {
                    //Damontodo 第一页
                    if (respectedTime < viewPagerRealLeft) {
                        selectedTime = viewPagerRealLeft;
                    }
                    Log.w(TAG, "第一星期了1-->>" + new Date(selectedTime * 1000L));
                } else if (mCurrentIndexWeek == (childCountWeek - 1)) {
                    //Damontodo 最后一页
                    if (respectedTime > viewPagerRealRight) {
                        selectedTime = viewPagerRealRight;
                    }
                    Log.w(TAG, "最后一星期了0-->>" + new Date(selectedTime * 1000L));
                }
                CalendarOneScreenDataWeek dataCurr = TestData.generateOneScreenDataWeek(selectedTime, viewPagerRealLeft, viewPagerRealRight);
                if (viewsWeek[realPositionCurr] == null) {
                    CalendarCard view = new CalendarCard(getContext(), dataCurr);
                    view.setId(View.generateViewId());
                    viewsWeek[realPositionCurr] = view;
                    viewsWeek[realPositionCurr].setOnCellClickListener(new CellClickListenerWeek());
//                    Log.w(TAG, "week, curr,重新初始化-->>" + new Date(selectedTime * 1000L));
                } else {
                    viewsWeek[realPositionCurr].dataWeek = dataCurr;
                    viewsWeek[realPositionCurr].postInvalidate();
//                    Log.w(TAG, "week, curr,更新数据-->>" + new Date(selectedTime * 1000L));
                }
            } else {
                Log.e(TAG, "week, curr越界了-->>" + mCurrentIndexWeek);
            }
        }
        if (realPositionNext < childCountWeek) {
            Calendar calendarNext = Calendar.getInstance();
            calendarNext.setTimeInMillis(unix * 1000L);
            calendarNext.add(Calendar.WEEK_OF_YEAR, 1);
            //期望选中的天
            int respectedTime = (int) (calendarNext.getTimeInMillis() / 1000L);
            long selectedTime = respectedTime;
            if (realPositionNext == (childCountWeek - 1)) {
                //Damontodo 最后一页
                if (respectedTime > viewPagerRealRight) {
                    selectedTime = viewPagerRealRight;
                }
                Log.w(TAG, "最后一星期了1-->>" + new Date(selectedTime * 1000L));
            }
            CalendarOneScreenDataWeek dataNext = TestData.generateOneScreenDataWeek(selectedTime, viewPagerRealLeft, viewPagerRealRight);

            if (viewsWeek[realPositionNext] == null) {
                CalendarCard view = new CalendarCard(getContext(), dataNext);
                view.setId(View.generateViewId());
                viewsWeek[realPositionNext] = view;
                viewsWeek[realPositionNext].setOnCellClickListener(new CellClickListenerWeek());
//                Log.w(TAG, "week, next重新初始化-->>" + new Date(selectedTime * 1000L));
            } else {
                viewsWeek[realPositionNext].dataWeek = dataNext;
                viewsWeek[realPositionNext].postInvalidate();
//                Log.w(TAG, "week, next,更新数据-->>" + new Date(selectedTime * 1000L));
            }
        } else {
            Log.e(TAG, "week, next越界了-->>" + realPositionNext);
        }
    }

    private class PagerScrollListenerWeek implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected, week-->>" + position + ", isInitWeekData-->>" + isInitWeekData);
            if (!isInitWeekData) {
                isInitWeekData = true;
                return;
            }

            mCurrentIndexWeek = calendarViewPagerWeek.getCurrentItem();
            CalendarOneScreenDataWeek dataCurr = viewsWeek[mCurrentIndexWeek].dataWeek;
            DayData selectedDayData = dataCurr.getMapDay().get(dataCurr.getSelectedPosition());
            long unixSelected = selectedDayData.getUnix();
            //重新设置周视图位置
//            int realPositionCurr = mCurrentIndexMonth % VIEWPAGER_SIZE;
            initViewsWeek(unixSelected, false);
            setMonthCurrent(unixSelected, true);
            updateTodayState();
            setDateTitle(unixSelected);
            updateTitleColor();
            int selectedPosition = viewsMonth[mCurrentIndexMonth].dataMonth.getSelectedPosition();
            unFoldingTop = calendarRowHeight * CalendarConstant.getRowFromPosition(selectedPosition, true);
//            Log.d(TAG, "onPageSelected, 周视图Pager选择-->>weekIndex-->>" + position + " unFoldingTop-->>" + unFoldingTop + " monthIndex-->>" + mCurrentIndexMonth + ", unixSelected-->>" + new Date(unixSelected * 1000L));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setMonthCurrent(long unixSelected, boolean isUpdateCurrent) {
        int mCurrentIndexMonthT = DateUtil.getSelectedPagerMonthIndex(viewPagerRealLeft, unixSelected);
        isInitMonthData = mCurrentIndexMonthT == mCurrentIndexMonth;
        mCurrentIndexMonth = mCurrentIndexMonthT;
        //重新初始化月视图数据
        initViewsMonth(unixSelected, isUpdateCurrent);
        calendarViewPagerMonth.setCurrentItem(mCurrentIndexMonth);
    }

    private class PagerScrollListenerMonth implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected, month-->>" + position + ", isInitMonthData-->>" + isInitMonthData);
            mCurrentIndexMonth = calendarViewPagerMonth.getCurrentItem();
            if (!isInitMonthData) {
                isInitMonthData = true;
                return;
            }
            //Damontodo 这里  需要放在onPageScrollStateChanged中,时间有限暂时不优化
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CalendarOneScreenDataMonth dataCurr = viewsMonth[mCurrentIndexMonth].dataMonth;
                    final int selectedPosition = dataCurr.getSelectedPosition();
                    DayData selectedDayData = dataCurr.getMapDay().get(selectedPosition);
                    final long unixSelected = selectedDayData.getUnix();
                    initViewsMonth(unixSelected, false);
                    setWeekCurrent(unixSelected, true);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            updateTodayState();
                            setDateTitle(unixSelected);
                            updateTitleColor();
                            //重新设置周视图位置
                            unFoldingTop = calendarRowHeight * CalendarConstant.getRowFromPosition(selectedPosition, true);
                        }
                    });
                }
            }).start();
//            Log.d(TAG, "onPageSelected, 月视图Pager选择-->>monthIndex-->>" + position + " unFoldingTop-->>" + unFoldingTop + " weekIndex-->>" + mCurrentIndexWeek + ", unixSelected-->>" + unixSelected);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    Log.d(TAG, "scrollState-->>SCROLL_STATE_DRAGGING");
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    Log.d(TAG, "scrollState-->>SCROLL_STATE_IDLE");
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    Log.d(TAG, "scrollState-->>SCROLL_STATE_SETTLING");
                    break;
                default:
                    //don't forget default
                    break;
            }

        }
    }

    private void setWeekCurrent(long unixSelected, boolean isUpdateCurrent) {
        int mCurrentIndexWeekT = DateUtil.getSelectedPagerWeekIndex(viewPagerRealLeft, unixSelected);
        isInitWeekData = mCurrentIndexWeekT == mCurrentIndexWeek;
        mCurrentIndexWeek = mCurrentIndexWeekT;
        //重新初始化周视图数据
        initViewsWeek(unixSelected, isUpdateCurrent);
        calendarViewPagerWeek.post(new Runnable() {
            @Override
            public void run() {
                CalendarAdapter calendarAdapterWeek = new CalendarAdapter<>(viewsWeek);
                calendarAdapterWeek.setType("week");
                //Damontodo: very import 这里必须重新执行 setAdapter 否则 weekPager可能显示不出来.
                calendarViewPagerWeek.setAdapter(calendarAdapterWeek);
                calendarViewPagerWeek.setCurrentItem(mCurrentIndexWeek);
            }
        });
    }

    private class CellClickListenerMonth implements CalendarCard.OnCellClickListener {


        /**
         * @param data  被选中的 CustomDate数据
         * @param index
         * @param row
         */
        @Override
        public void clickDate(DayData data, int index, int row) {
            unFoldingTop = calendarRowHeight * row;
            setWeekCurrent(data.getUnix(), true);
            updateTodayState();
            setDateTitle(data.getUnix());
            updateTitleColor();
//            Log.d(TAG, "CellClickListenerMonth-->>unFoldingTop-->>" + unFoldingTop);

        }
    }

    private class CellClickListenerWeek implements CalendarCard.OnCellClickListener {


        /**
         * @param data  被选中的 CustomDate数据
         * @param index
         * @param row
         */
        @Override
        public void clickDate(DayData data, int index, int row) {
            setMonthCurrent(data.getUnix(), true);
            updateFoldingTop();
            updateTodayState();
            setDateTitle(data.getUnix());
            updateTitleColor();
//            Log.d(TAG, "CellClickListenerWeek-->>unFoldingTop-->>" + unFoldingTop + ", monthRow-->>" + monthRow);
        }
    }

    /**
     * 更新今天按钮状态;
     */
    @UiThread
    private void updateTodayState() {
        ((MainActivity) getContext()).setToDayIconState(cellSelectedIsToday());
    }

    /**
     * 更新折叠的位置
     */
    private void updateFoldingTop() {
        int position = viewsMonth[mCurrentIndexMonth].dataMonth.getSelectedPosition();
        int monthRow = CalendarConstant.getRowFromPosition(position, true);
        unFoldingTop = calendarRowHeight * monthRow;
    }

    /**
     * 设置 指示器布局是否显示
     */
    private void setIndicatorLayoutVisible(boolean open) {
//        Log.d(TAG, "closeIndicator-->>distance-->>" + distance+", indicatorLayoutState-->>"+indicatorLayoutState);
        if (!open) {
            //关闭指示器布局
            if (indicatorLayoutState == INDICATOR_CLOSEING || indicatorLayoutState == INDICATOR_CLOSE) {
                return;
            }
            if (indicatorLayoutState == INDICATOR_OPENING) {
                indicatorScroller.forceFinished(true);
            }
            indicatorLayoutState = INDICATOR_CLOSEING;
            int hideHeight = colorIndicatorLayoutHeight - indicatorLayoutOffsetPx;
            indicatorScroller.startScroll(0, colorIndicatorLayoutHeight, 0, -hideHeight, DURATION_COLOR_LAYOUT);
            post(indicatorCloseRunnable);
        } else {
            //打开指示器布局
            if (indicatorLayoutState == INDICATOR_OPENING || indicatorLayoutState == INDICATOR_OPEN) {
                return;
            }
            if (indicatorLayoutState == INDICATOR_CLOSEING) {
                indicatorScroller.forceFinished(true);
            }
            indicatorLayoutState = INDICATOR_OPENING;
            int hideHeight = colorIndicatorLayoutHeight - indicatorLayoutOffsetPx;
            indicatorScroller.startScroll(0, indicatorLayoutOffsetPx, 0, hideHeight, DURATION_COLOR_LAYOUT);
            post(indicatorOpenRunnable);
        }
    }

    /**
     * 初始化各种Runnable
     */
    private void initRunnable() {
        //还原到月日历模式的Runnable
        scrollRunnable0 = new Runnable() {
            @Override
            public void run() {
                upScroller.computeScrollOffset();
//                Log.d(TAG, "滚动执行-->>" + upScroller.getCurrY()+",scrollLayout.getScrollY()-->>"+scrollLayout.getScrollY());
                scrollLayout.scrollTo(0, upScroller.getCurrY());
                setContentParams();
                if (!upScroller.isFinished()) {
                    post(this);
                } else {
                    setWeekMode(false);
                }
            }
        };

        scrollRunnable2 = new Runnable() {
            @Override
            public void run() {
                upScroller2.computeScrollOffset();
//                                Log.d(TAG, "滚动执行-->>" + upScroller2.getCurrY()+",scrollLayout.getScrollY()-->>"+scrollLayout.getScrollY());
                int scrollY = upScroller2.getCurrY();
                scrollLayout.scrollTo(0, scrollY);
                if (scrollY < unFoldingTop) {
                    setWeekMode(false);
                } else if (unFoldingTop == scrollY) {
                    setWeekMode(unFoldingTop != 0);
                } else {
                    setWeekMode(true);
                }
                setContentParams();
                if (!upScroller2.isFinished()) {
                    post(this);
                }
            }
        };
        indicatorCloseRunnable = new Runnable() {
            @Override
            public void run() {
                indicatorScroller.computeScrollOffset();
                ViewGroup.LayoutParams params = colorIndicatorLayout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = indicatorScroller.getCurrY();
//                Log.d(TAG, "params.height-->>" + params.height);
                colorIndicatorLayout.setLayoutParams(params);
                if (!indicatorScroller.isFinished()) {
                    post(this);
                } else {
                    indicatorLayoutState = INDICATOR_CLOSE;
                }
            }
        };
        indicatorOpenRunnable = new Runnable() {
            @Override
            public void run() {
                indicatorScroller.computeScrollOffset();
                ViewGroup.LayoutParams params = colorIndicatorLayout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = indicatorScroller.getCurrY();
//                Log.d(TAG, "params.height-->>" + params.height);
                colorIndicatorLayout.setLayoutParams(params);
                if (!indicatorScroller.isFinished()) {
                    post(this);
                } else {
                    indicatorLayoutState = INDICATOR_OPEN;
                }
            }
        };
    }

    /**
     * @param isTrue true:轴视图模式 false:月视图模式
     */
    private void setWeekMode(boolean isTrue) {
        if (isTrue) {
            calendarViewPagerWeek.setVisibility(View.VISIBLE);
            setIndicatorLayoutVisible(false);
            pagerMode = PAGER_MODE_WEEK;

        } else {
            calendarViewPagerWeek.setVisibility(View.INVISIBLE);
            setIndicatorLayoutVisible(true);
            pagerMode = PAGER_MODE_MONTH;
        }
    }

    /**
     * 更新Title相关颜色信息
     */
    @UiThread
    private void updateTitleColor() {
        Utils.setTitleColor((MainActivity) getContext(), getSelectedDayType());
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collapse_scrollview_color_detail:
                Log.d(TAG, "点击了 经期颜色详情 按钮");
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.startActivity(new Intent(mainActivity, MensColorDetailActivity.class));
                break;

        }
    }

//    ***************************************
//             对外提供设置参数
//    ***************************************

    /**
     * 必须设置(一般在获取该对象后就调用此方法)
     *
     * @param left  日历开始日期的边界
     * @param right 日历结束日期的边界
     */
    @UiThread
    public void setPagerBoundary(int left, int right) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(left * 1000L);
        c.set(Calendar.DAY_OF_MONTH, 1);
        viewPagerRealLeft = DateUtil.get0000Unix1(DateUtil.getTimeUnix(c.getTimeInMillis()));

        c.setTimeInMillis(right * 1000L);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DATE, -1);
        viewPagerRealRight = DateUtil.get0000Unix1(DateUtil.getTimeUnix(c.getTimeInMillis()));

        //初始化Pager数据
        setPagerData();
    }


    /**
     * 设置标题日期的内容
     *
     * @param selectedUnix 选中日期的unix
     */
    @UiThread
    public void setDateTitle(long selectedUnix) {
        ((MainActivity) getContext()).setDateTitle(selectedUnix);
    }


    /**
     * 1.切换到月视图模式
     * 2.月视图的index为今天所在的月
     * 3.选中的cell为今天
     */
    @UiThread
    public void moveToToday() {

        if (cellSelectedIsToday()) {
            //已经选中今天了
            Log.d(TAG, "moveToToday-->>当前为月视图模式&&选中今天了");
            return;
        }
        Log.d(TAG, "moveToToday-->>当前视图模式-->>" + pagerMode);
        long todayUnix = DateUtil.getTodayUnix();
        setWeekCurrent(todayUnix, true);
        setMonthCurrent(todayUnix, true);
        if (pagerMode == PAGER_MODE_WEEK) {
            //当前为周视图模式
            int y = scrollLayout.getScrollY();
            int duration = getDuration(Math.abs(y));
            upScroller2.startScroll(0, y, 0, -y, duration);
            post(scrollRunnable2);
        }
        updateTodayState();
        setDateTitle(getSelectedUnix());
        //更新折叠的位置
        updateFoldingTop();
    }

    @UiThread
    public void moveToYearMonth(int year, int month) {
        long unix = getSelectedUnix();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(unix * 1000L);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        unix = c.getTimeInMillis()/1000L;
        setWeekCurrent(unix, true);
        setMonthCurrent(unix, true);
        if (pagerMode == PAGER_MODE_WEEK) {
            //当前为周视图模式
            int y = scrollLayout.getScrollY();
            int duration = getDuration(Math.abs(y));
            upScroller2.startScroll(0, y, 0, -y, duration);
            post(scrollRunnable2);
            descriptionLayout.fullScroll(ScrollView.FOCUS_UP);
        }

        updateTodayState();
        setDateTitle(getSelectedUnix());
        //更新折叠的位置
        updateFoldingTop();
    }

    /**
     * @return true: 表示周视图或月视图选中的cell是今天
     */
    public boolean cellSelectedIsToday() {
        long unix = getSelectedUnix();
//        Log.d(TAG, "当前月视图,选中的日期为-->>" + DateUtil.printDate(unix));
        return DateUtil.get0000Unix1(unix) == DateUtil.getTodayUnix();
    }

    /**
     * @return 获取选中天的unix
     */
    public long getSelectedUnix() {
        CalendarOneScreenDataMonth oneScreenDataMonth = viewsMonth[mCurrentIndexMonth].dataMonth;
        int position = oneScreenDataMonth.getSelectedPosition();
        return oneScreenDataMonth.getMapDay().get(position).getUnix();
    }

    /**
     * @return 获取选中天的DayType
     */
    @CalendarConstant.DayType
    public int getSelectedDayType() {
        CalendarOneScreenDataMonth oneScreenDataMonth = viewsMonth[mCurrentIndexMonth].dataMonth;
        int position = oneScreenDataMonth.getSelectedPosition();
        return oneScreenDataMonth.getMapDay().get(position).getDayType();
    }


}
