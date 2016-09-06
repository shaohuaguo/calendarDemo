package calendar.bigkoo.pickerview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bigkoo.pickerview.R;

/**
 * 弹出的框
 * Created by longfei.zhang on 2016/1/20.
 */
public class PickerBaseDialog extends AlertDialog{
    public PickerBaseDialog(Context context) {
        super(context);
    }

    public PickerBaseDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        super.show();
        //小米手机在show之前设置有问题
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setAttributes(params);
        mWindow.setWindowAnimations(R.style.AnimBottom);


    }
}
