package calendar.bigkoo.pickerview.utils;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by longfei.zhang on 2016/1/20.
 */
public class PickerViewUtils {
    /**
     * 获取1-12月字符串，中文1月2月。。。
     * @return 1-12月字符串集合
     */
    public static ArrayList<String> getMonths(){
        ArrayList<String> months1 = new ArrayList<>();
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String[] months = symbols.getShortMonths();
        for (int i = 0; i < months.length; i++) {
            months1.add(months[i]);
        }
        return months1;
    }
}
