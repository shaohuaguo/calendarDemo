package calendar.bigkoo.pickerview.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import calendar.bigkoo.pickerview.dialog.PickerBaseDialog;
import calendar.bigkoo.pickerview.utils.PickerViewAnimateUtil;
import com.bigkoo.pickerview.R;
import calendar.bigkoo.pickerview.listener.OnDismissListener;

/**
 * Created by Sai on 15/11/22.
 * 精仿iOSPickerViewController控件
 */
public class BasePickerView {
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );

    private Context context;
    protected ViewGroup contentContainer;
//    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//附加View 的 根View

    private OnDismissListener onDismissListener;
    private boolean isDismissing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.BOTTOM;
    private PickerBaseDialog mDialog;
    /**
     * 是否可以点击周围消失，默认false不可以
     */
    private boolean isCancelOutSide = false;
    /**
     * 是否可以点击返回键消失，默认false不可以
     */
    private boolean isCancelBack = false;
    public BasePickerView(Context context){
        this.context = context;

        initViews();
        init();
        initEvents();
    }

    protected void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        decorView = (ViewGroup) ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_basepickerview, null);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.setLayoutParams(params);
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }
    protected void initEvents() {
    }
    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
//        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }

    private void alertDialog(View view){
        mDialog = new PickerBaseDialog(context);
        mDialog.setCanceledOnTouchOutside(isCancelOutSide);
        mDialog.setCancelable(isCancelBack);
        mDialog.show();
        mDialog.setContentView(view);
    }
    /**
     * 添加这个View到Dialog //Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        alertDialog(rootView);
//        onAttached(rootView);
    }
    private void setCanceledOnTouchOutside(boolean isCancel){
        this.isCancelOutSide = isCancel;
    }
    private void setCancelBack(boolean isCancel){
        this.isCancelBack = isCancel;
    }
//    /**
//     * 检测该View是不是已经添加到根视图
//     *
//     * @return 如果视图已经存在该View返回true
//     */
//    public boolean isShowing() {
//        mDialog.isShowing();
//        View view = decorView.findViewById(R.id.outmost_container);
//        return view != null;
//    }

    public boolean isShowing() {
        if(mDialog == null){
            return false;
        }
      return  mDialog.isShowing();
    }
    public void dismiss() {
        if (isDismissing) {
            return;
        }

        if(mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
            isDismissing = true;
        }
//        //消失动画
//        outAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                decorView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //从activity根视图移除
//                        decorView.removeView(rootView);
//                        isDismissing = false;
//                        if (onDismissListener != null) {
//                            onDismissListener.onDismiss(BasePickerView.this);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        contentContainer.startAnimation(outAnim);

    }
    public Animation getInAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public BasePickerView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        }
        else{
            view.setOnTouchListener(null);
        }
        return this;
    }
    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public View findViewById(int id){
        return contentContainer.findViewById(id);
    }
}
