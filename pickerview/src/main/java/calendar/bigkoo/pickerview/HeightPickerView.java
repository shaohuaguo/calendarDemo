package calendar.bigkoo.pickerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.R;

import calendar.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import calendar.bigkoo.pickerview.lib.WheelView;
import calendar.bigkoo.pickerview.view.BasePickerView;
import calendar.bigkoo.pickerview.view.SwitchButton;

import java.util.ArrayList;

/**
 * 身高选择
 * Created by longfei.zhang on 2016/1/11.
 */
public class HeightPickerView<T> extends BasePickerView implements View.OnClickListener,
        SwitchButton.SwitchClickCallBack{

    private static final String TAG = "HeightPickerView";
    private int mCmMax = Constant.HEIGHT_CM_MAX;
    private int mCmMin = Constant.HEIGHT_CM_MIN;
    private int mFtMax = Constant.HEIGHT_FT_MAX;
    private int mFtMin = Constant.HEIGHT_FT_MIN;
    private int mInMax = 11;
    private int mInMin = 0;
    /**
     * 一英尺==30.48cm
     */
    private static final float FT2CM = 30.48f;
    /**
     * 一英寸==2.54cm
     */
    private static final float IN2CM = 2.54f;
    private Context mContext;
    private String mTitle;
    //0cm 1in
    private int unit = 0;

    private SwitchButton mSwitchBtn;
    private View mConfirmBtn, mCancelBtn;
    private TextView mTitleTv;

    private LinearLayout mCmLayout;
    private LinearLayout mInLayout;

    private WheelView mCmWv;
    private WheelView mFtWv;
    private WheelView mInWv;

    private OnHeightSelectListener mSelectListener;
    private ArrayList<Integer> mCmList = new ArrayList<>();

    private ArrayList<Integer> mInList = new ArrayList<>();
    private ArrayList<Integer> mInList1 = new ArrayList<>();
    /**
     * 选中的值
     */
    private int mSelectValue = Constant.HEIGHT_DEFAULT;

    public HeightPickerView(Context context) {
        super(context);
        this.mContext = context;
        init1();
    }

    /**
     *
     * @param context context
     * @param title 标题
     * @param unit 0cm 1IN
     * @param currentValue 选中的值
     */
    public HeightPickerView(Context context, String title, int unit, int currentValue) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.unit = unit;
        this.mSelectValue = currentValue;
        Log.d(TAG, "unit:" + unit);
        init1();
    }
    /**
     *
     * @param context context
     * @param title 标题
     * @param unit 0cm 1IN
     */
    public HeightPickerView(Context context, String title, int unit) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.unit = unit;
        Log.d(TAG, "unit:" + unit);
        init1();
    }
    private void init1(){

        LayoutInflater.from(mContext).inflate(R.layout.pickerview_height, contentContainer);

        mSwitchBtn = (SwitchButton)findViewById(R.id.topbar_switch_btn);
        mSwitchBtn.setCallBack(this);
        // -----确定和取消按钮
        mConfirmBtn = findViewById(R.id.topbar_confirm_btn);
        mCancelBtn = findViewById(R.id.topbar_cancel_btn);
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        //顶部标题
        mTitleTv = (TextView) findViewById(R.id.topbar_title_tv);

        mCmLayout = (LinearLayout)findViewById(R.id.picker_height_cm_layout);
        mInLayout = (LinearLayout)findViewById(R.id.picker_height_in_layout);

        mCmWv = (WheelView)findViewById(R.id.picker_height_cm_wv);
        mFtWv = (WheelView)findViewById(R.id.picker_height_ft_wv);
        mInWv = (WheelView)findViewById(R.id.picker_height_in_wv);
        mFtWv.setTextSize(Constant.TEXT_SIZE);
        mInWv.setTextSize(Constant.TEXT_SIZE);
        mCmWv.setTextSize(Constant.TEXT_SIZE);
        mFtWv.setCyclic(false);
        mInWv.setCyclic(false);
        mCmWv.setCyclic(false);

        mCmWv.setLabel(Constant.HEIGHT_CM);
        mFtWv.setLabel(Constant.HEIGHT_FT);
        mInWv.setLabel(Constant.HEIGHT_IN);

        initData();
    }
    private void initData(){
        mCmList.clear();
        mInList.clear();
        mInList1.clear();

        for(int i=mCmMin;i<=mCmMax;i++){
            mCmList.add(i);
        }
        for(int i= mFtMin;i<= mFtMax;i++){
            mInList.add(i);
        }
        for(int i=mInMin;i<=mInMax;i++){
            mInList1.add(i);
        }
        mSwitchBtn.setSelectWhich(unit);
        mCmWv.setAdapter(new ArrayWheelAdapter(mCmList));
        mFtWv.setAdapter(new ArrayWheelAdapter(mInList));
        mInWv.setAdapter(new ArrayWheelAdapter(mInList1));


        if(unit == Constant.SWITCH_LEFT){
            //cm的默认选中
            int cmSelect = mCmList.indexOf(mSelectValue);
            if(cmSelect!=-1){
                mCmWv.setCurrentItem(cmSelect);
            }else{
                Log.d(TAG, "没有选择的:" + mSelectValue);
            }
            //in的默认选中
            cmToIn(mSelectValue);
        }else{
            inToCm(mSelectValue);
            inToft(mSelectValue);
        }



        clickWhichChange(unit);

    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.topbar_cancel_btn)
        {
            dismiss();
        } else if(view.getId()==R.id.topbar_confirm_btn) {
            if(mSelectListener!=null){
                if(mSwitchBtn.getSelectWhich() == 0){
                    int value = (int)mCmWv.getAdapter().getItem(mCmWv.getCurrentItem());
                    String str = value+Constant.HEIGHT_CM;
                    mSelectListener.onHeightSelect(str, value, unit);
                }else{
                    int ft = (int)mFtWv.getAdapter().getItem(mFtWv.getCurrentItem());
                    int in = (int)(mInWv.getAdapter().getItem(mInWv.getCurrentItem()));
                    int inValue = ftToIn(ft, in);
                    String str = ft+Constant.HEIGHT_FT;
                    String str1 = in+Constant.HEIGHT_IN;
                    mSelectListener.onHeightSelect(str+" "+str1, inValue, unit);
                }

            }
            dismiss();
        }
    }

    public int getCmMax() {
        return mCmMax;
    }

    public void setCmMax(int mCmMax) {
        this.mCmMax = mCmMax;
        initData();
    }

    public int getCmMin() {
        return mCmMin;
    }

    public void setCmMin(int mCmMin) {
        this.mCmMin = mCmMin;
        initData();
    }

    public int getInMax() {
        return mFtMax;
    }

    public void setInMax(int mInMax) {
        this.mFtMax = mInMax;
        initData();
    }

    public int getInMin() {
        return mFtMin;
    }

    public void setInMin(int mInMin) {
        this.mFtMin = mInMin;
        initData();
    }

    public OnHeightSelectListener getOnSelectListener() {
        return mSelectListener;
    }

    public void setOnSelectListener(OnHeightSelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }

    @Override
    public void clickLeft() {
    }

    @Override
    public void clickRight() {
    }


    @Override
    public void clickWhichChange(int index) {
        switch (index){
            case Constant.SWITCH_LEFT:
                unit = Constant.SWITCH_LEFT;
                mCmLayout.setVisibility(View.VISIBLE);
                mInLayout.setVisibility(View.GONE);
                break;
            case Constant.SWITCH_RIGHT:
                unit = Constant.SWITCH_RIGHT;
                mCmLayout.setVisibility(View.GONE);
                mInLayout.setVisibility(View.VISIBLE);
                break;
        }
    }



    public interface OnHeightSelectListener{
        /**
         * @param str 选中拼好的字符串
         * @param value cm值
         * @param unit 0cm 1IN
         * */
        void onHeightSelect(String str, int value, int unit);
    }

    private void cmToIn(int value){
        if(value<=0){
            return;
        }
        int temp = (int)(value/FT2CM);
        if(temp< mFtMin){
            temp = mFtMin;
        }else if(temp> mFtMax){
            temp = mFtMax;
        }
        int temp1 = Math.round(((value - temp * FT2CM) / IN2CM));
        if (temp1 < mInMin) {
            temp1 = mInMin;
        } else if (temp1 > mInMax) {
            temp1 = mInMax;
        }
        RaiingPickerLog.d(TAG, "cmToIn, cm:"+value+", ft"+temp+", in:"+temp1);
        mFtWv.setCurrentItem(mInList.indexOf(temp));
        mInWv.setCurrentItem(mInList1.indexOf(temp1));
    }

    private int inToCm(int in){
        int temp = Math.round(in*IN2CM);
        if(temp<mCmMin){
            temp = mCmMin;
        }else if(temp>mCmMax){
            temp = mCmMax;
        }
        RaiingPickerLog.d(TAG, "in:"+in+", inToCm转化：" + temp);
        mCmWv.setCurrentItem(mCmList.indexOf(temp));
        return temp;
    }
    private int ftToIn(int ft, int in){
        if(ft< mFtMin){
            ft = mFtMin;
        }else if(ft> mFtMax){
            ft = mFtMax;
        }
        int temp1 = ft*12 + in;
        RaiingPickerLog.d(TAG, "ft:" +ft+",in:"+in+", ftToIn转化：" + temp1);
        return temp1;
    }

    private void inToft(int in){
        int temp = in/12;
        int temp1 = in - temp*12;
        if(temp< mFtMin){
            temp = mFtMin;
        }else if(temp> mFtMax){
            temp = mFtMax;
        }

        if(temp1<mInMin){
            temp1 = mInMin;
        }else if(temp1>mInMax){
            temp1 = mInMax;
        }
        RaiingPickerLog.d(TAG, "inToft初始值：" + in + ", ft:" + temp + ", in:" + temp1);
        mFtWv.setCurrentItem(mInList.indexOf(temp));
        mInWv.setCurrentItem(mInList1.indexOf(temp1));
    }
}
