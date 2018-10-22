package com.zhsl.pcmsv2.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取某一date的年份和月份
     * @param date
     * @return format example： 1992-8
     */
    public static String getYearAndMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        StringBuilder sb = new StringBuilder();
        return sb.append(cal.get(Calendar.YEAR)).append("-").append(cal.get(Calendar.MONTH) + 1).toString();
    }
}
