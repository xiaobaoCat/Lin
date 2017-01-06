package com.lin.demo.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

    public static String dateFormatStr = "yyyy-MM-dd HH:mm";
    public static String dateFormatStr2 = "yyyy-MM-dd";


   
    
    
    
    private static final long MINUTE_SECONDS = 60; //1分钟多少秒
	private static final long HOUR_SECONDS = MINUTE_SECONDS*60;
	private static final long DAY_SECONDS = HOUR_SECONDS*24;
	private static final long YEAR_SECONDS = DAY_SECONDS*365;
	 
	@SuppressLint("SimpleDateFormat")
	public static String gettime_DHcl(long oldMilliseconds) {
		
		SimpleDateFormat format =  new SimpleDateFormat("MM"+"月"+"dd"+"日");  
		Date date1=new Date(oldMilliseconds);
		String date =format.format(date1);
		return date;
		
	}
	@SuppressLint("SimpleDateFormat")
	public static String gettimecl(long oldMilliseconds) {
		
		 SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        Date date1=new Date(oldMilliseconds);
	        String date =format.format(date1);
			return date;
		
	}
	public static String testPassedTime(long oldMilliseconds) {
	   long passed = (System.currentTimeMillis()-oldMilliseconds) /1000;//转为秒
	   if (passed > YEAR_SECONDS) {
	       return passed/YEAR_SECONDS+"年";
	   } else if (passed > DAY_SECONDS) {
	       return passed/DAY_SECONDS+"天";
	   } else if (passed > HOUR_SECONDS) {
	       return passed/HOUR_SECONDS+"小时";
	   } else if (passed > MINUTE_SECONDS) {
	       return passed/MINUTE_SECONDS+"分钟";
	   } else {
	       return passed+"秒";
	   }
	}
    
    /**
     * yyyy-MM-dd HH:MM 转成long 型时间
     * @param dateTime
     * @return
     */
    public static long getTimeByString(String dateTime, String format) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            date = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return date.getTime();
        } else {
            return 0;
        }
    }

    /**
     * 获取yyyy-MM-dd hh:mm类型
     *
     * @return
     */
    public static String getDateFormat(String str, String formatStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            return format.format(format.parse(str)).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone gmt = TimeZone.getTimeZone("GMT+08:00");
        format.setTimeZone(gmt);
        String current = format.format(Calendar.getInstance().getTime());
        return current;
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        TimeZone gmt = TimeZone.getTimeZone("GMT+08:00");
        format.setTimeZone(gmt);
        String current = format.format(time);
        return current;
    }

    //"yyyy-MM-dd HH:mm:ss"
    public static String getTimes(long time,String formatString ) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        TimeZone gmt = TimeZone.getTimeZone("GMT+08:00");
        format.setTimeZone(gmt);
        String current = format.format(time);
        return current;
    }


    // 返回的日期格式为2012-01-01等格式
    public static String getFormateTimeformCal(Calendar cal) {
        String strTime = "";
        strTime += cal.get(Calendar.YEAR);
        strTime += "-";
        strTime += getFormateInteger(cal.get(Calendar.MONTH) + 1);
        strTime += "-";
        strTime += getFormateInteger(cal.get(Calendar.DATE));
        return strTime;
    }

    private static String getFormateInteger(int time) {
        String strTime = "";
        if (time < 10) {
            strTime = "0" + Integer.toString(time);
        } else {
            strTime = Integer.toString(time);
        }
        return strTime;
    }

    /**
     * 获取该日期前后几天
     * @param date yyyy-MM-dd
     * @param jumpIndex 正数为后几天，负数为前几天
     * @return
     */
    public static String getDateAddOrSub(String date,int jumpIndex) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int dayOfYear ;
        try {
            calendar.setTime(df.parse(date));
            dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            dayOfYear +=jumpIndex;
            calendar.set(Calendar.DAY_OF_YEAR,dayOfYear);
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String todayDate = format1.format(calendar.getTime());
            return todayDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取该月份前后几月
     * @param date yyyy-MM
     * @param jumpIndex 正数为后几月，负数为前几月
     * @return
     */
    public static String getMonthAddOrSub(String date,int jumpIndex) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        int MONTH ;
        try {
            calendar.setTime(df.parse(date));
            MONTH = calendar.get(Calendar.MONTH);
            MONTH +=jumpIndex;
            calendar.set(Calendar.MONTH, MONTH);
            DateFormat format1 = new SimpleDateFormat("yyyy-MM");
            String todayDate = format1.format(calendar.getTime());
            return todayDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 2天内可填写日志
     *
     * @param dateString
     * @return
     */
    public static boolean canAddLogByDate(String dateString) {
        try {
            Calendar date = Calendar.getInstance();
            date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateString));

            Calendar now = Calendar.getInstance();
            now.setTime(new Date());

            if (now.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR) <= 2 && now.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR) >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
            return false;
        }
    }


    /**
     * 获取今天是第几天
     *
     * @return
     */
    public static int getDayOfYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_YEAR);// 今天日期常看昨天日志
    }

    /**
     * 根据第几天获取日期字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dayofyear
     * @return
     */
    public static String getDateTimeStringByIndex(int dayofyear) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, dayofyear);
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayDate = format1.format(cal.getTime());
        return todayDate;

    }


    /**
     * 分解yyyy-dd-mmThh:mm:ss 得到hh:mm
     * @param s
     * @return
     */
    public static String getTime(String s) {
        if (s.contains("T")) {
            try {
                int index = s.lastIndexOf("T");
                return s.subSequence(index + 1, 16).toString();
            } catch (Exception e) {
                // TODO: handle exception
                return s;
            }
        } else {
            try {
                return s.subSequence(11, 16).toString();
            } catch (Exception e) {
                return "";
            }
        }
    }

    /**
     * 根据第几天获取日期字符串
     * @param dayofyear
     * @return
     */
    public static String getDateStringByIndex(int dayofyear) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, dayofyear);
        DateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd");
        String todayDate = format1.format(cal.getTime());
        return todayDate;
    }

    /**
     *根据日期获取年份
     * @param date 日期 yyyy-mm-dd
     * @return
     */
    public static int getYearByDate(String date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(date.split("-")[0]));
        cal.set(Calendar.MONTH, Integer.valueOf(date.split("-")[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.split("-")[2]));
        return cal.get(Calendar.YEAR);
    }

    /**
     *根据日期获取月份
     * @param date 日期 yyyy-mm-dd
     * @return
     */
    public static int getMonthByDate(String date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(date.split("-")[0]));
        cal.set(Calendar.MONTH, Integer.valueOf(date.split("-")[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.split("-")[2]));
        return cal.get(Calendar.MONTH)+1;
    }

    /**
     *根据日期获取日
     * @param date 日期 yyyy-mm-dd
     * @return
     */
    public static int getDayByDate(String date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(date.split("-")[0]));
        cal.set(Calendar.MONTH, Integer.valueOf(date.split("-")[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.split("-")[2]));
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    /**
     * 获取当前年份
     * @return
     */
    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }
    /**
     * 获取当前月份
     * @return
     */
    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH)+1;
    }
    /**
     * 获取当前月份
     * @return
     */
    public static int getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 日期格式转为yyyy年mm月dd日
     *
     * @param s
     * @return
     */
    public static String addWordForm(String s) {
        if (s.length() >= 8) {
            s = remove_form(s);
        }
        // System.out.println("日期格式转化" + s);
        try {
            s = s.substring(0, 4) + "年" + s.substring(4, 6) + "月"
                    + s.substring(6, 8) + "日";
            // s = s.substring(0, 4) + "-" + s.substring(4, 6) + "-"
            // + s.substring(6, 8) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return s;
        }
        return s;
    }

    /**
     * 日期格式转为yyyymmdd
     *
     * @param s
     * @return
     */
    public static String remove_form(String s) {
        s = s.substring(0, 4) + s.substring(5, 7) + s.substring(8, 10);
        return s;
    }


    /**
     * 返回当前 年 月 日 的int[ 年，月，日，时，分，秒]
     * @return
     */
    public static int[] getCurDateNumber() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        return new int[] { year, month, day, hour, mins, sec };
    }


    /**
     * 获取当前时间差
     * @param mLastTime 传入的时间
     * @return
     */
    public static String getLastTime(long mLastTime) {
        long newTime = new Date().getTime() - mLastTime;
        int newSecondTime = (int) (newTime / 1000);
        if (newTime < 0) {
            return null;
        }
        if (newSecondTime <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (newSecondTime < 60) {
            sb.append(newSecondTime + "s前");
        } else {
            int minutes = (newSecondTime / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    int days = hours / 24;
                    if (days>30){
                        Date date = new Date(mLastTime);
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        sb.append(simpleDateFormat.format(date));
                    }else{
                        sb.append(days+"天前");
                    }
                } else {
                    sb.append(hours + "小时前");
                }
            } else {
                sb.append(minutes + "分钟前");
            }
        }
        return sb.toString();
    }
}




