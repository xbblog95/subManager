package com.xbblog.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @date: 2018/6/19 9:21
 * @description: 日期工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final Log logger = LogFactory.getLog(DateUtils.class);

    public static final String DATE_SHORT_FORMAT = "yyyyMMdd";
    public static final String DATE_CH_FORMAT = "yyyy年MM月dd日";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_MONTH_FORMAT = "yyyy-MM";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String DAYTIME_START = "00:00:00";
    public static final String DAYTIME_END = "23:59:59";

    private static final String[] FORMATS = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH:mm:ss", "HH:mm", "HH:mm:ss", "yyyy-MM",
            "yyyy-MM-dd HH:mm:ss.S"};

    private DateUtils() {

    }


    /**
     * 将日期转换成format字符串
     * @param date 例如: Sun Jun 10 09:18:00 CST 2018
     * @param dateFormat 例如: "yyyy-MM-dd HH:mm:ss"
     */
    public static String dateFormat(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }
        if (null == dateFormat) {
            dateFormat = DATE_TIME_FORMAT;
        }
        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 根据传入的日期  转换成这样格式的字符串 如：“yyyy-MM-dd HH:mm:ss”
     */
    public static String dateFormat(Date date) {
        return dateFormat(date, DATE_TIME_FORMAT);
    }


    public static Long getTimeDiff(String startTime, String endTime) {
        int months = 0;
        Long days = null;
        Date startDate = null;
        Date endDate = null;
        try {
            if (startTime.length() == 10 && endTime.length() == 10) {
                startDate = new SimpleDateFormat(DATE_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_FORMAT).parse(endTime);
                days = getTimeDiff(startDate, endDate);
            } else if (startTime.length() == 7 && endTime.length() == 7) {
                startDate = new SimpleDateFormat(DATE_MONTH_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_MONTH_FORMAT).parse(endTime);
                months = getMonthDiff(startDate, endDate);
                days = new Long((long) months);
            } else {
                startDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(endTime);
                days = getTimeDiff(startDate, endDate);
            }
        } catch (ParseException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage());
            }
            days = null;
        }
        return days;
    }

    /**
     * 返回两个时间的相差天数
     */
    public static Long getTimeDiff(Date startTime, Date endTime) {
        Long days = null;

        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        long l_s = c.getTimeInMillis();
        c.setTime(endTime);
        long l_e = c.getTimeInMillis();
        days = (l_e - l_s) / MILLIS_PER_DAY;
        return days;
    }

    /**
     * 返回两个时间的相差分钟数
     */
    public static Long getMinuteDiff(Date startTime, Date endTime) {
        Long minutes = null;

        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        long l_s = c.getTimeInMillis();
        c.setTime(endTime);
        long l_e = c.getTimeInMillis();
        minutes = (l_e - l_s) / MILLIS_PER_MINUTE;
        return minutes;
    }

    /**
     * 返回两个时间的相差秒数
     */
    public static Long getSecondDiff(Date startTime, Date endTime) {
        return (endTime.getTime() - startTime.getTime()) / MILLIS_PER_SECOND;
    }

    public static Date getNextDate(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }
    /**
     * 返回两个时间的相差月数
     */
    public static int getMonthDiff(Date startTime, Date endTime) {
        int months = 0;

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        endCalendar.setTime(endTime);
        months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return months;
    }

    /**
     * 返回时间对象的日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDateDate(Date date) throws ParseException {
        if(date == null)
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        Date dateDate =  sdf.parse(s);
        return dateDate;
    }

    public static Date getDateNextDate(Date date) throws ParseException {
        Date dateDate =  getDateDate(date);
        return getNextDate(dateDate);
    }
}