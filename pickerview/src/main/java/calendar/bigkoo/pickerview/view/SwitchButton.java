package calendar.bigkoo.pickerview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.R;

/**
 * 选择按钮
 * Created by longfei.zhang on 2016/1/11.
 */
public class SwitchButton extends RelativeLayout implements View.OnClickListener{

    private static final String TAG = "SwitchButton";
    private Context mContext;
    private TextView mLeftText;
    private TextView mRightText;
    /**
     * 左文本
     */
    private String mleftTextStr;
    /**
     * 右文本
     */
    private String mRightTextStr;
    /**
     * 字体大小
     */
    private float mTextSize;
    /**
     * 选中的text字体颜色
     */
    private int mSelectTextColor;
    /**
     * 选择的text背景颜色
     */
    private int mSelectTextColorBg;
    /**
     * 未选中的text字体颜色
     */
    private int mUnSelectTextColor;
    /**
     * 未选中的text背景颜色
     */
    private int mUnSelectTextColorBg;
    /**
     * 0选中左，1选中右,默认选择左
     */
    private int mSelectWhich = 0;



    private SwitchClickCallBack mCallBack;
    public SwitchButton(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.switch_button);
        mleftTextStr = ta.getString(R.styleable.switch_button_switch_left_text);
        mRightTextStr = ta.getString(R.styleable.switch_button_switch_right_text);
        mTextSize = ta.getDimension(R.styleable.switch_button_switch_text_size, -1);
        mSelectTextColor = ta.getColor(R.styleable.switch_button_select_text_color, getResources().getColor(R.color.pickerview_white));
        mSelectTextColorBg = ta.getColor(R.styleable.switch_button_select_text_color_bg, getResources().getColor(R.color.pickerview_timebtn_nor));
        mUnSelectTextColor = ta.getColor(R.styleable.switch_button_unselect_text_color, getResources().getColor(R.color.pickerview_timebtn_nor));
        mUnSelectTextColorBg = ta.getColor(R.styleable.switch_button_unselect_text_color_bg, getResources().getColor(R.color.pickerview_bg_topbar));
        mSelectWhich = ta.getInt(R.styleable.switch_button_select_which, 0);
        init();
    }


    private void init(){
        LayoutInflater.from(mContext).inflate(R.layout.switch_button, this);
        mLeftText = (TextView)findViewById(R.id.switch_left_text);
        mRightText = (TextView)findViewById(R.id.switch_right_text);
        if(mTextSize != -1){
            mLeftText.setTextSize(mTextSize);
            mRightText.setTextSize(mTextSize);
            Log.d(TAG, "字体大小：" + mTextSize);
        }else{
            Log.d(TAG, "默认字体大小：" + mLeftText.getTextSize());
        }
        if(!TextUtils.isEmpty(mleftTextStr)){
            mLeftText.setText(mleftTextStr);
        }
        if(!TextUtils.isEmpty(mRightTextStr)){
            mRightText.setText(mRightTextStr);
        }
        //设置选中哪个
        setClickSelectColor(mSelectWhich);
        mLeftText.setOnClickListener(this);
        mRightText.setOnClickListener(this);
    }

    public TextView getLeftText() {
        return mLeftText;
    }

    public void setLeftText(TextView mLeftText) {
        this.mLeftText = mLeftText;
    }

    public TextView getRightText() {
        return mRightText;
    }

    public void setRightText(TextView mRightText) {
        this.mRightText = mRightText;
    }

    public int getSelectTextColor() {
        return mSelectTextColor;
    }

    public void setSelectTextColor(int mSelectTextColor) {
        this.mSelectTextColor = mSelectTextColor;
    }

    public int getSelectTextColorBg() {
        return mSelectTextColorBg;
    }

    public void setSelectTextColorBg(int mSelectTextColorBg) {
        this.mSelectTextColorBg = mSelectTextColorBg;
    }

    public int getUnSelectTextColor() {
        return mUnSelectTextColor;
    }

    public void setUnSelectTextColor(int mUnSelectTextColor) {
        this.mUnSelectTextColor = mUnSelectTextColor;
    }

    public int getUnSelectTextColorBg() {
        return mUnSelectTextColorBg;
    }

    public void setUnSelectTextColorBg(int mUnSelectTextColorBg) {
        this.mUnSelectTextColorBg = mUnSelectTextColorBg;
    }

    public int getSelectWhich() {
        return mSelectWhich;
    }

    public void setSelectWhich(int mSelectWhich) {
        this.mSelectWhich = mSelectWhich;
        setClickSelectColor(mSelectWhich);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public String getLeftTextStr() {
        return mleftTextStr;
    }

    public void setLeftTextStr(String mleftTextStr) {
        this.mleftTextStr = mleftTextStr;
    }

    public String getRightTextStr() {
        return mRightTextStr;
    }

    public void setRightTextStr(String mRightTextStr) {
        this.mRightTextStr = mRightTextStr;
    }

    public SwitchClickCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(SwitchClickCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.switch_left_text){
            Log.d(TAG, "点击左事件");

            if(mSelectWhich!=0){
                mSelectWhich = 0;
                if(mCallBack!=null){
                    mCallBack.clickWhichChange(0);
                }
            }
            setClickSelectColor(0);
            if(mCallBack!=null){
                mCallBack.clickLeft();
            }
        }else if(view.getId() == R.id.switch_right_text){
            Log.d(TAG, "点击右事件");
            if(mSelectWhich!=1){
                mSelectWhich = 1;
                if(mCallBack!=null){
                    mCallBack.clickWhichChange(1);
                }
            }

            setClickSelectColor(1);
            if(mCallBack!=null){
                mCallBack.clickRight();
            }
        }else{
            Log.d(TAG, "没有这个点击事件");
        }
    }

    /**
     * 设置点击的背景变化
     * @param index
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setClickSelectColor(int index){
        switch (index){
            case 0:
                mLeftText.setTextColor(mSelectTextColor);
                mLeftText.setBackground(getResources().getDrawable(R.drawable.shape_left_tv));
                mRightText.setTextColor(mUnSelectTextColor);
                mRightText.setBackground(getResources().getDrawable(R.drawable.shape_right_tv_un));
                break;
            case 1:
                mLeftText.setTextColor(mUnSelectTextColor);
                mLeftText.setBackground(getResources().getDrawable(R.drawable.shape_left_tv_un));
                mRightText.setTextColor(mSelectTextColor);
                mRightText.setBackground(getResources().getDrawable(R.drawable.shape_right_tv));
                break;
        }

    }

    public interface SwitchClickCallBack{
        void clickLeft();
        void clickRight();

        /**
         * 点击的选择是否有变化
         * @param index
         */
        void clickWhichChange(int index);
    }
}
