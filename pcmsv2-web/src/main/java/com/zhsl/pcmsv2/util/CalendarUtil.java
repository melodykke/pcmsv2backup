package com.zhsl.pcmsv2.util;

import com.zhsl.pcmsv2.model.BaseInfo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    // 计算当前日期和开工日期之间的天数差值
    public static int calDifferDays(Date startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = dateFormat.format(new Date());
        String commence = dateFormat.format(startDate);
        try {
            Date nowDate = dateFormat.parse(now);
            Date commenceDate = dateFormat.parse(commence);
            int days = (int) ((nowDate.getTime() - commenceDate.getTime()) / (1000 * 3600 * 24));
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
