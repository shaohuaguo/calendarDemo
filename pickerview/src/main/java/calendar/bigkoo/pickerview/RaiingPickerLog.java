package calendar.bigkoo.pickerview;

import android.util.Log;

/**
 * Created by longfei.zhang on 2016/1/13.
 */
public class RaiingPickerLog {
    public static void d(String tag, String message){
        if(Constant.DEBUG){
            Log.d(tag, message);
        }

    }
}
