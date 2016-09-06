package com.gsh.calendar;

import java.util.Calendar;
import java.util.Date;

public class NewDateDifference {

    public static void main(String[] args) {

        test0();
    }

    public static void test0() {

//        System.out.println("language-->>" + getContext().getResources().getConfiguration().locale.getCountry());
//        System.out.println("timeZone name-->>" + TimeZone.getDefault().getDisplayName());
//        System.out.println("timeZone offset-->>" + TimeZone.getDefault().getRawOffset());
        String[] insert1 = new String[]{"3", "11", "2015"};
        String[] insert2 = new String[]{"20", "3", "2016"};


        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(insert1[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(insert1[1]));
        cal.set(Calendar.YEAR, Integer.parseInt(insert1[2]));
        Date firstDate = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(insert2[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(insert2[1]));
        cal.set(Calendar.YEAR, Integer.parseInt(insert2[2]));
        Date secondDate = cal.getTime();


        long diff = secondDate.getTime() - firstDate.getTime();

        System.out.println("Days0: " + diff / 1000 / 60 / 60 / 24);
        System.out.println("Days1: " + getDiffDay(insert1, insert2));
    }

    public static int getDiffDay(String[] insert1, String[] insert2) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(insert1[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(insert1[1]));
        cal.set(Calendar.YEAR, Integer.parseInt(insert1[2]));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int unix1 = (int) (cal.getTimeInMillis() / 1000L);

        Calendar cal2 = Calendar.getInstance();

        cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(insert2[0]));
        cal2.set(Calendar.MONTH, Integer.parseInt(insert2[1]));
        cal2.set(Calendar.YEAR, Integer.parseInt(insert2[2]));
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        int unix2 = (int) (cal2.getTimeInMillis() / 1000L);

        int result = 0;
        for (int i = 0; i < 1000; i++) {
            cal2.add(Calendar.DATE, unix1 > unix2 ? 1 : -1);
            if (cal2.getTimeInMillis() / 1000L == unix1) {
                result = i + 1;
                break;
            }
        }
        return result;
    }
}
