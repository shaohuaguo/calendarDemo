package calendar.bigkoo.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.R;

import calendar.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import calendar.bigkoo.pickerview.adapter.NumericWheelAdapter;
import calendar.bigkoo.pickerview.lib.WheelView;
import calendar.bigkoo.pickerview.listener.OnItemSelectedListener;
import calendar.bigkoo.pickerview.utils.PickerViewUtils;
import calendar.bigkoo.pickerview.view.BasePickerView;
import calendar.bigkoo.pickerview.view.SwitchButton;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 年月日 月年 年月
 * Created by longfei.zhang on 2016/1/20.
 */
public class YMDPickerView <T> extends BasePickerView implements View.OnClickListener{


    public enum Type {
        YEAR_MONTH_DAY, MONTH_YEAR, YEAR_MONTH
    }
    private Context mContext;
    private String mTitle;

    private SwitchButton mSwitchBtn;
    private View mConfirmBtn, mCancelBtn;
    private TextView mTitleTv;


    private WheelView mYearWv;
    private WheelView mMonthWv;
    private WheelView mDayWv;

    private Type mType = Type.YEAR_MONTH_DAY;

    //前后一百年
    private int mStartYear;
    private int mEndYear;

    /**
     * 最小时间必须大于这个时间 秒 1899/12/31 23:59
     */
    private long mMinTimeDefault = -2209017660L;
    /**
     * 选中的时间
     */
    private long mInitTime = mMinTimeDefault;
    /**
     * 最小时间 秒
     */
    private long mMinTime = mMinTimeDefault;
    /**
     * 最大时间 秒
     */
    private long mMaxTime = mMinTimeDefault;
    // 添加大小月月份并将其转换为list,方便之后的判断
    private String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
    private String[] months_little = { "4", "6", "9", "11" };
    private List<String> list_big = Arrays.asList(months_big);
    private List<String> list_little = Arrays.asList(months_little);

    private OnYMDSelectListener mListener;
    /**
     *
     * @param context context
     * @param type YEAR_MONTH_DAY, MONTH_YEAR
     * @param title 标题
     * @param initTime 初始时间 秒
     */
    public YMDPickerView(Context context, Type type, String title, long initTime) {
        super(context);
        this.mContext = context;
        this.mType = type;
        this.mTitle = title;
        this.mInitTime = initTime;
        init1();
        initData();
    }
    /**
     *
     * @param context context
     * @param type YEAR_MONTH_DAY, MONTH_YEAR
     * @param title 标题
     */
    public YMDPickerView(Context context, Type type, String title) {
        super(context);
        this.mContext = context;
        this.mType = type;
        this.mTitle = title;
        init1();
        initData();
    }
    /**
     *
     * @param context context
     * @param title 标题
     * @param initTime 初始时间 秒
     */
    public YMDPickerView(Context context, String title, long initTime) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.mInitTime = initTime;
        init1();
        initData();
    }
    private void init1(){
        LayoutInflater.from(mContext).inflate(R.layout.pickerview_ymd, contentContainer);

        mSwitchBtn = (SwitchButton)findViewById(R.id.topbar_switch_btn);
        mSwitchBtn.setVisibility(View.INVISIBLE);
        // -----确定和取消按钮
        mConfirmBtn = findViewById(R.id.topbar_confirm_btn);
        mCancelBtn = findViewById(R.id.topbar_cancel_btn);
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        //顶部标题
        mTitleTv = (TextView) findViewById(R.id.topbar_title_tv);
        mTitleTv.setText(mTitle);

        mYearWv = (WheelView)findViewById(R.id.picker_ymd_year_wv);
        mMonthWv = (WheelView)findViewById(R.id.picker_ymd_month_wv);
        mDayWv = (WheelView)findViewById(R.id.picker_ymd_day_wv);
        mYearWv.setTextSize(Constant.TEXT_SIZE);
        mMonthWv.setTextSize(Constant.TEXT_SIZE);
        mDayWv.setTextSize(Constant.TEXT_SIZE);
        //中国需要有单位
        if(mContext.getResources().getConfiguration().locale.getCountry().equals(
                Locale.CHINA.getCountry())){
            mYearWv.setLabel(mContext.getResources().getString(R.string.pickerview_year));
            mDayWv.setLabel(mContext.getResources().getString(R.string.pickerview_day));
        }else{
            mType = Type.MONTH_YEAR;
        }

        setItemSelectListener();
    }

    private void initData(){

        Calendar initCalendar = Calendar.getInstance();
        if(mInitTime!=mMinTimeDefault){
            initCalendar.setTimeInMillis(mInitTime*1000L);
        }

        int year = initCalendar.get(Calendar.YEAR);
        int month = initCalendar.get(Calendar.MONTH);
        int day = initCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        mStartYear = curYear - 100;
        mEndYear = curYear + 100;

        if(mType == Type.YEAR_MONTH_DAY || mType == Type.YEAR_MONTH){
            mYearWv.setAdapter(new NumericWheelAdapter(mStartYear, mEndYear));
            mMonthWv.setAdapter(new ArrayWheelAdapter(PickerViewUtils.getMonths()));
            mYearWv.setCurrentItem(year - mStartYear);
            mMonthWv.setCurrentItem(month);
            if(mType == Type.YEAR_MONTH_DAY ){
                setDayAdapter(year, month);
                if(day > mDayWv.getItemsCount()){
                    //不可能出现
                    day = mDayWv.getItemsCount();
                }
            }else{
                mDayWv.setVisibility(View.GONE);
            }

            mDayWv.setCurrentItem(day - 1);
        }else if(mType == Type.MONTH_YEAR){
            mMonthWv.setAdapter(new NumericWheelAdapter(mStartYear, mEndYear));
            mYearWv.setAdapter(new ArrayWheelAdapter(PickerViewUtils.getMonths()));
            mMonthWv.setCurrentItem(year - mStartYear);
            mYearWv.setCurrentItem(month);
            mDayWv.setVisibility(View.GONE);
        }

    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.topbar_cancel_btn)
        {
            dismiss();
        } else if(view.getId()==R.id.topbar_confirm_btn) {
            confirmTime();
            dismiss();
        }
    }
    //确定时间
    private void confirmTime(){
        if(mListener!=null){
            int year = 0;
            int month = 0;
            int day = 1;
            Calendar calendar = Calendar.getInstance();

            if(mType == Type.YEAR_MONTH_DAY){
                year = mYearWv.getCurrentItem() + mStartYear;
                month = mMonthWv.getCurrentItem();
                day = mDayWv.getCurrentItem() + 1;
                calendar.set(year, month, day, 0, 0);
                mListener.onYMDSelect(calendar, year, month, day);
            }else if(mType == Type.MONTH_YEAR){
                year = mMonthWv.getCurrentItem() + mStartYear;
                month = mYearWv.getCurrentItem();
                calendar.set(year, month, 1, 0, 0);
                mListener.onYMDSelect(calendar, year, month);
            }else if(mType == Type.YEAR_MONTH){
                year = mYearWv.getCurrentItem() + mStartYear;
                month = mMonthWv.getCurrentItem();
                calendar.set(year, month, 2, 0, 0);
                mListener.onYMDSelect(calendar, year, month);
            }

        }
    }
    private void setItemSelectListener(){
        mYearWv.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if(mType == Type.YEAR_MONTH_DAY){
                    setDayAdapter(index + mStartYear, mMonthWv.getCurrentItem());
                }else if(mType == Type.MONTH_YEAR){
                    setDayAdapter(mMonthWv.getCurrentItem() + mStartYear, index);
                }
                checkSelectTime();
            }
        });
        mMonthWv.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if(mType == Type.YEAR_MONTH_DAY){
                    setDayAdapter(mYearWv.getCurrentItem() + mStartYear, index);
                }else if(mType == Type.MONTH_YEAR){
                    setDayAdapter(index + mStartYear, mYearWv.getCurrentItem());
                }
                checkSelectTime();
            }
        });

        mDayWv.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                checkSelectTime();
            }
        });
    }

    private void setDayAdapter(int year, int month){
        // 判断大小月及是否闰年,用来确定"日"的数据
        int maxItem = 30;
        if (list_big
                .contains(String.valueOf(month + 1))) {
            mDayWv.setAdapter(new NumericWheelAdapter(1, 31));
            maxItem = 31;
        } else if (list_little.contains(String.valueOf(month + 1))) {
            mDayWv.setAdapter(new NumericWheelAdapter(1, 30));
            maxItem = 30;
        } else {
            if ((year % 4 == 0 && year % 100 != 0)
                    || year % 400 == 0) {
                mDayWv.setAdapter(new NumericWheelAdapter(1, 29));
                maxItem = 29;
            } else {
                mDayWv.setAdapter(new NumericWheelAdapter(1, 28));
                maxItem = 28;
            }
        }
        if (mDayWv.getCurrentItem() > maxItem - 1) {
            mDayWv.setCurrentItem(maxItem - 1);
        }
    }
    private void checkSelectTime(){
        int ySel = 0;
        int mSel = 0;
        int dSel = 0;
        if(mType == Type.YEAR_MONTH_DAY){
            ySel = mYearWv.getCurrentItem() + mStartYear;
            mSel = mMonthWv.getCurrentItem();
            dSel = mDayWv.getCurrentItem() + 1;
        }else if(mType == Type.MONTH_YEAR){
            ySel = mMonthWv.getCurrentItem() + mStartYear;
            mSel = mYearWv.getCurrentItem();
            dSel = 1;
        }else if(mType == Type.YEAR_MONTH){
            ySel = mYearWv.getCurrentItem() + mStartYear;
            mSel = mMonthWv.getCurrentItem();
            dSel = 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(ySel, mSel, dSel, 0, 0);
        long selTime = calendar.getTimeInMillis()/1000;
        if(mMinTime!=mMinTimeDefault && selTime<mMinTime){
            mInitTime = mMinTime;
            initData();
        }
        if(mMaxTime!=mMinTimeDefault && selTime>mMaxTime){
            mInitTime = mMaxTime;
            initData();
        }
    }
    /**
     *
     * @return 单位秒
     */
    public long getMinTime() {
        return mMinTime;
    }
    /**
     * 单位秒
     */
    public void setMinTime(long mMinTime) {

        this.mMinTime = mMinTime;
    }
    /**
     * 单位秒
     */
    public long getMaxTime() {
        return mMaxTime;
    }
    /**
     * 单位秒
     */
    public void setMaxTime(long mMaxTime) {
        this.mMaxTime = mMaxTime;
    }

    public OnYMDSelectListener getOnYMDSelecttListener() {
        return mListener;
    }

    public void setOnYMDSelectListener(OnYMDSelectListener mListener) {
        this.mListener = mListener;
    }

    public interface OnYMDSelectListener {
        /**
         * 点击确定的回调
         * @param calendar calendar
         * @param year 2016
         * @param month 0-11
         * @param day 1-31
         */
        void onYMDSelect(Calendar calendar, int year, int month, int day);
        /**
         * 点击确定的回调
         * @param calendar calendar
         * @param year 2016
         * @param month 0-11
         */
        void onYMDSelect(Calendar calendar, int year, int month);
    }

}
