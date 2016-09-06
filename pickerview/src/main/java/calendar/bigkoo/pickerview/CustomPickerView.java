package calendar.bigkoo.pickerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.R;

import calendar.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import calendar.bigkoo.pickerview.lib.WheelView;
import calendar.bigkoo.pickerview.view.BasePickerView;
import calendar.bigkoo.pickerview.view.SwitchButton;

import java.util.ArrayList;

/**
 * 单个两个滚动选择
 * Created by longfei.zhang on 2016/1/13.
 */
public class CustomPickerView<T> extends BasePickerView implements View.OnClickListener{

    private static final String TAG = "SingleDoublePickerView";
    private Context mContext;
    private ArrayWheelAdapter mAdapter1;
    private ArrayWheelAdapter mAdapter2;
    private WheelView mWv1;
    private WheelView mWv2;
    private SwitchButton mSb;
    private View mConfirmBtn, mCancelBtn;
    private TextView mTitleTv;
    private String mTitle;
    private boolean isShowSb = false;

    private OnCustomSelectListener mListener;
    /**
     * 默认单行1,两行2
     */
    private int mRowNum = 1;
    public CustomPickerView(Context context, String title,  ArrayList<T> data){
        super(context);
        this.mContext = context;
        if(TextUtils.isEmpty(title)){
            return;
        }
        if(data == null || !(data.size()>0)){
            return;
        }

        this.mTitle = title;
        mAdapter1 = new ArrayWheelAdapter(data);
        mRowNum = 1;
        init1();
    }
    public CustomPickerView(Context context, String title, ArrayList<T> data1, ArrayList<T> data2){
        super(context);
        this.mContext = context;
        if(TextUtils.isEmpty(title)){
            return;
        }
        if(data1 == null || !(data1.size()>0)){
            return;
        }
        if(data2 == null || !(data2.size()>0)){
            return;
        }
        this.mTitle = title;

        mAdapter1 = new ArrayWheelAdapter(data1);
        mAdapter2 = new ArrayWheelAdapter(data2);
        mRowNum = 2;
        init1();
    }
    private void init1(){
        LayoutInflater.from(mContext).inflate(R.layout.pickerview_custom, contentContainer);
        mWv1 = (WheelView)findViewById(R.id.picker_singledouble_wv1);
        mWv2 = (WheelView)findViewById(R.id.picker_singledouble_wv2);
        mSb = (SwitchButton)findViewById(R.id.topbar_switch_btn);
        // -----确定和取消按钮
        mConfirmBtn = findViewById(R.id.topbar_confirm_btn);
        mCancelBtn = findViewById(R.id.topbar_cancel_btn);
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        //顶部标题
        mTitleTv = (TextView) findViewById(R.id.topbar_title_tv);
        mTitleTv.setText(mTitle);
        if(!isShowSb){
            mSb.setVisibility(View.INVISIBLE);
        }

        mWv1.setAdapter(mAdapter1);
        mWv1.setTextSize(Constant.TEXT_SIZE);
        mWv1.setCyclic(false);
        if(mRowNum == 2){
            mWv2.setAdapter(mAdapter2);
            mWv2.setTextSize(Constant.TEXT_SIZE);
            mWv2.setCyclic(false);
        }else if(mRowNum == 1){
            mWv2.setVisibility(View.GONE);
        }
    }

    /**
     * @param object 要选择的值
     */
    public void setSelectItem(Object object){
        if(object == null){
            RaiingPickerLog.d(TAG, "设置的值不能为空");
            return;
        }
        int position = mAdapter1.indexOf(object);
        mWv1.setCurrentItem(position);
    }

    /**
     * @param position 设定选择的位置
     */
    public void setSelectItem(int position){
        if(position < 0){
            RaiingPickerLog.d(TAG, "选择的位置不能为负");
            return;
        }
        mWv1.setCurrentItem(position);
    }

    /**
     * @param object1 第一列要选择的值
     * @param object2 第二列要选择的值
     */
    public void setSelectItem(Object object1, Object object2){
        if(object1 == null || object2 == null){
            RaiingPickerLog.d(TAG, "设置的值不能为空");
            return;
        }
        int position1 = mAdapter1.indexOf(object1);
        int position2 = mAdapter2.indexOf(object2);
        mWv1.setCurrentItem(position1);
        mWv2.setCurrentItem(position2);
    }

    /**
     * @param position1 第一列设定选择的位置
     * @param position2 第二列设定选择的位置
     */
    public void setSelectItem(int position1, int position2){
        if(position1 < 0|| position2 < 0){
            RaiingPickerLog.d(TAG, "选择的位置不能为负");
            return;
        }
        mWv1.setCurrentItem(position1);
        mWv2.setCurrentItem(position2);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.topbar_cancel_btn)
        {
            dismiss();
        } else if(view.getId()==R.id.topbar_confirm_btn) {
            if(mListener!=null){
                switch (mRowNum){
                    case 1:
                        int position = mWv1.getCurrentItem();
                        RaiingPickerLog.d(TAG, "选择的position：" + position);
                        mListener.onSingleSelect(mAdapter1.getItem(position), position);
                        break;
                    case 2:

                        int postion1 = mWv1.getCurrentItem();
                        int postion2 = mWv2.getCurrentItem();
                        RaiingPickerLog.d(TAG, "选择的position：" + postion1 + ", " + postion2);
                        mListener.onDoubleSelect(mAdapter1.getItem(postion1), postion1,
                                mAdapter2.getItem(postion2), postion2);
                        break;
                    default:
                        break;
                }
            }
            dismiss();
        }
    }


    public void setOnSelectListener(OnCustomSelectListener mListener) {
        this.mListener = mListener;
    }

    public interface OnCustomSelectListener {
        void onSingleSelect(Object object, int position);
        void onDoubleSelect(Object object1, int position1, Object object2, int position2);
    }
}
