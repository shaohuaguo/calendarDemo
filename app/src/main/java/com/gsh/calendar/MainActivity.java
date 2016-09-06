package com.gsh.calendar;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import calendar.bigkoo.pickerview.YMDPickerView;
import com.gsh.calendar.util.DateUtil;
import com.gsh.calendar.util.Utils;
import com.gsh.calendar.widget.CollapseScrollView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity-->>";

    private CollapseScrollView collapseScrollView;

    /**
     * 帮助内容左上角的小箭头
     */
    private RelativeLayout calendarTitleArrowUp;

    /**
     * 帮助内容(问号点开后显示)
     */
    private TextView calendarHelpContent;

    /**
     * 今天按钮
     */
    private ImageView calendarTitleToday;

    /**
     * 标题日期 比如: 2015年10月
     */
    private TextView calendarTitleDate;

    //pager允许滑动的最小时间: 1443974400 ==2015/10/05 00:00
    private int leftUnix = 1443974400;
    //pager允许滑动的最大时间:1467648000 ==2016/7/05 00:00
    private int rightUnix = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        initData();
    }

    private void initLayout() {
        collapseScrollView = (CollapseScrollView) findViewById(R.id.collapse_scrollview_layout);

        findViewById(R.id.calendar_title_content).setOnClickListener(this);
        findViewById(R.id.calendar_title_help).setOnClickListener(this);
        calendarTitleToday = (ImageView) findViewById(R.id.calendar_title_today);
        calendarTitleToday.setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        calendarTitleArrowUp = (RelativeLayout) findViewById(R.id.calendar_title_arrow_up);
        calendarHelpContent = (TextView) findViewById(R.id.calendar_help_content);
        calendarTitleDate = (TextView) findViewById(R.id.calendar_title_date);
    }

    private void initData() {
        setTestData();
        //初始化"今天"按钮的显示状态
        setToDayIconState(collapseScrollView.cellSelectedIsToday());
        setDateTitle(collapseScrollView.getSelectedUnix());
        Utils.setTitleColor(this, collapseScrollView.getSelectedDayType());
    }


    private void setTestData() {
        //Damontodo 测试
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,2);
        rightUnix = (int) (calendar.getTimeInMillis()/1000L);
        Log.d(TAG, "设置测试数据-->>setTestData");
        collapseScrollView.setPagerBoundary(leftUnix, rightUnix);

    }

    /**
     * 设置标题日期的内容
     *
     * @param selectedUnix 选中日期的unix
     */
    @UiThread
    public void setDateTitle(long selectedUnix) {
        calendarTitleDate.setText(DateUtil.formatTime0(selectedUnix, this));
    }

    /**
     * @param isShow true:显示"今天"按钮, false: 隐藏
     */
    @UiThread
    public void setToDayIconState(boolean isShow) {
        calendarTitleToday.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                Log.d(TAG, "单击了日历标题的 返回按钮");
                finish();
                break;
            case R.id.calendar_title_content:
                Log.d(TAG, "单击了日历标题的 内容按钮");
                showYMPicker();
                break;
            case R.id.calendar_title_today:
                Log.d(TAG, "单击了日历标题的 今天按钮");
                collapseScrollView.moveToToday();
                break;
            case R.id.calendar_title_help:
                Log.d(TAG, "单击了日历标题的 问号按钮");
                toggleHelpLayout(true);
                break;

            default:
                //don't forget default
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (helpLayoutIsShow()) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                toggleHelpLayout(false);
            }
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * @return true: 帮助布局已显示 false: 帮助布局隐藏
     */
    private boolean helpLayoutIsShow() {
        return calendarTitleArrowUp.getVisibility() == View.VISIBLE;
    }

    /**
     * @param isShow true:显示帮助布局, false: 不显示
     */
    private void toggleHelpLayout(boolean isShow) {
        calendarTitleArrowUp.setVisibility(isShow ? View.VISIBLE : View.GONE);
        calendarHelpContent.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void showYMPicker() {
        final long selectTime = collapseScrollView.getSelectedUnix();
        String yearMonth = getString(R.string.year_moth);
        YMDPickerView ymdPickerView = new YMDPickerView(this, YMDPickerView.Type.YEAR_MONTH, yearMonth, selectTime);
        ymdPickerView.setOnYMDSelectListener(new YMDPickerView.OnYMDSelectListener() {
            @Override
            public void onYMDSelect(Calendar calendar, int year, int month, int day) {
            }

            @Override
            public void onYMDSelect(Calendar calendar, int year, int month) {
                //点击确定时,调用
//                long unix = (calendar.getTimeInMillis() / 1000L);
//                Log.d(TAG, "选择的是,year-->>" + year + ", month-->>" + month+", unix-->>"+unix+", selectTime-->>"+selectTime);
                collapseScrollView.moveToYearMonth(year, month);
            }
        });
        ymdPickerView.setMinTime(leftUnix);
        ymdPickerView.setMaxTime(rightUnix);
        ymdPickerView.show();
    }
}
