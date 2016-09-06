package calendar.bigkoo.pickerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.R;

import calendar.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import calendar.bigkoo.pickerview.lib.WheelView;
import calendar.bigkoo.pickerview.view.BasePickerView;
import calendar.bigkoo.pickerview.view.SwitchButton;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 体重
 * Created by longfei.zhang on 2016/1/13.
 */
public class WeightPickerView <T> extends BasePickerView implements View.OnClickListener,
        SwitchButton.SwitchClickCallBack{
    private static final String TAG = "WeightPickerView";
    /**
     * 1kg==0.45359237 lb
     */
    public static final float KG2LB = 0.45359237f;
    //有的国家语言小数点为其他符号，统一用小数点
    DecimalFormatSymbols symbol = new DecimalFormatSymbols(Locale.US);
    DecimalFormat mDecimalFormat;
    private Context mContext;
    private String mTitle;
    //0kg 1lbs
    private int unit = Constant.SWITCH_LEFT;

    private SwitchButton mSwitchBtn;
    private View mConfirmBtn, mCancelBtn;
    private TextView mTitleTv;


    private WheelView mIntWv;
    private WheelView mDecimalsWv;
    private WheelView mUnitWv;

    private OnWeightSelectListener mSelectListener;
    private ArrayList<Integer> mIntList = new ArrayList<>();

    private ArrayList<String> mDecimalsList = new ArrayList<>();
    private ArrayList<String> mUnitList = new ArrayList<>();
    /**
     * 选中的值
     */
    private int mSelectValueG = Constant.WEIGHT_DEFAULT;

    public WeightPickerView(Context context) {
        super(context);
        this.mContext = context;
        init1();
    }

    /**
     *
     * @param context context
     * @param title 标题
     * @param unit 0kg 1lbs
     * @param currentValue 选中的值 克为单位 如50kg是50*1000
     */
    public WeightPickerView(Context context, String title, int unit, int currentValue) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.unit = unit;
        this.mSelectValueG = currentValue;
        Log.d(TAG, "unit:" + unit);
        init1();
    }
    /**
     *
     * @param context context
     * @param title 标题
     * @param unit 0kg 1lbs
     */
    public WeightPickerView(Context context, String title, int unit) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.unit = unit;
        Log.d(TAG, "unit:" + unit);
        init1();
    }
    private void init1(){
        mDecimalFormat = new DecimalFormat("#.0", symbol);
        LayoutInflater.from(mContext).inflate(R.layout.pickerview_weight, contentContainer);

        mSwitchBtn = (SwitchButton)findViewById(R.id.topbar_switch_btn);
        mSwitchBtn.setCallBack(this);
        mSwitchBtn.getLeftText().setText(mContext.getResources().getString(R.string.weight_kg));
        mSwitchBtn.getRightText().setText(mContext.getResources().getString(R.string.weight_lbs));
        // -----确定和取消按钮
        mConfirmBtn = findViewById(R.id.topbar_confirm_btn);
        mCancelBtn = findViewById(R.id.topbar_cancel_btn);
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        //顶部标题
        mTitleTv = (TextView) findViewById(R.id.topbar_title_tv);
        mTitleTv.setText(mTitle);


        mIntWv = (WheelView)findViewById(R.id.picker_weight_int_wv);
        mDecimalsWv = (WheelView)findViewById(R.id.picker_weight_decimals_wv);
        mUnitWv = (WheelView)findViewById(R.id.picker_weight_unit_wv);
        mIntWv.setTextSize(Constant.TEXT_SIZE);
        mDecimalsWv.setTextSize(Constant.TEXT_SIZE);
        mUnitWv.setTextSize(Constant.TEXT_SIZE);

        mIntWv.setCyclic(false);
        mDecimalsWv.setCyclic(false);
        mUnitWv.setCyclic(false);

        initData();
    }
    private void initData(){
        mIntList.clear();
        mDecimalsList.clear();
        mUnitList.clear();

        if(unit == Constant.SWITCH_LEFT){
            for(int i=Constant.WEIGHT_KG_MIN;i<=Constant.WEIGHT_KG_MAX;i++){
                mIntList.add(i);
            }
            for(int i=0;i<=9;i++){
                mDecimalsList.add("."+i);
            }
            mUnitList.add(Constant.HEIGHT_KG);
        }else{
            for(int i=Constant.WEIGHT_LBS_MIN;i<=Constant.WEIGHT_LBS_MAX;i++){
                mIntList.add(i);
            }
            for(int i=0;i<=9;i++){
                mDecimalsList.add("."+i);
            }
            mUnitList.add(Constant.HEIGHT_LBS);
        }

        mIntWv.setAdapter(new ArrayWheelAdapter(mIntList));
        mDecimalsWv.setAdapter(new ArrayWheelAdapter(mDecimalsList));
        mUnitWv.setAdapter(new ArrayWheelAdapter(mUnitList));


        mSwitchBtn.setSelectWhich(unit);
        setSelect(mSelectValueG);


    }

    /**
     *
     * @param value 克
     */
    private void setSelect(int value){
        if(value<=0){
            return;
        }
        if(unit == Constant.SWITCH_LEFT){
            float f = value/1000f;
            String str = mDecimalFormat.format(f);
            RaiingPickerLog.d(TAG, String.valueOf(f));
            int num = Integer.valueOf(str.substring(0, str.length()-2));
            int deci = Integer.valueOf(str.substring(str.length()-1, str.length()));
            if(num>Constant.WEIGHT_KG_MAX){
                num = Constant.WEIGHT_KG_MAX;
            }else if(num < Constant.WEIGHT_KG_MIN){
                num = Constant.WEIGHT_KG_MIN;
            }
            int index = mIntList.indexOf(num);
            if(index != -1){
                mIntWv.setCurrentItem(index);
            }
            mDecimalsWv.setCurrentItem(deci);
        }else{
            float f = value/KG2LB/1000f;
            RaiingPickerLog.d(TAG,"转换的lbs：" + f);
            String str = mDecimalFormat.format(f);
            RaiingPickerLog.d(TAG, String.valueOf(f));
            int num = Integer.valueOf(str.substring(0, str.length()-2));
            int deci = Integer.valueOf(str.substring(str.length()-1, str.length()));
            if(num>Constant.WEIGHT_LBS_MAX){
                num = Constant.WEIGHT_LBS_MAX;
            }else if(num < Constant.WEIGHT_LBS_MIN){
                num = Constant.WEIGHT_LBS_MIN;
            }
            int index = mIntList.indexOf(num);
            if(index != -1){
                mIntWv.setCurrentItem(index);
            }
            mDecimalsWv.setCurrentItem(deci);
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.topbar_cancel_btn)
        {
            dismiss();
        } else if(view.getId()==R.id.topbar_confirm_btn) {
            if(mSelectListener!=null){
                if(mSwitchBtn.getSelectWhich() == Constant.SWITCH_LEFT){
                    String num = (int)mIntWv.getAdapter().getItem(mIntWv.getCurrentItem())+"";
                    int deci = mDecimalsWv.getCurrentItem();
                    String temp = num + mDecimalsList.get(deci);
                    int value = (int)(Float.valueOf(temp)*1000);
                    String str = temp+Constant.HEIGHT_KG;
                    mSelectListener.onWeightSelect(str, value, unit);
                }else{
                    String num = (int)mIntWv.getAdapter().getItem(mIntWv.getCurrentItem())+"";
                    int deci = mDecimalsWv.getCurrentItem();
                    float temp = Float.valueOf(num + mDecimalsList.get(deci));
                    String str = num + mDecimalsList.get(deci) + Constant.HEIGHT_LBS;
                    mSelectListener.onWeightSelect(str, lbsToKg(temp), unit);
                }

            }
            dismiss();
        }
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
            case 0:
                unit = Constant.SWITCH_LEFT;
                break;
            case 1:
                unit = Constant.SWITCH_RIGHT;
                break;
        }
        initData();
    }

    public OnWeightSelectListener getOnSelectListener() {
        return mSelectListener;
    }

    public void setOnSelectListener(OnWeightSelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }

    public interface OnWeightSelectListener {
        /**
         * @param str 选中拼好的字符串
         * @param value cm值
         * @param unit 0cm 1IN
         * */
        void onWeightSelect(String str, int value, int unit);
    }

    /**
     * lbs转为kg，返回值为g
     * @param value lbs
     * @return g
     */
    private int lbsToKg(float value){
        float temp = value*KG2LB*1000;
        int result = (int) temp;
        return result;
    }

}
