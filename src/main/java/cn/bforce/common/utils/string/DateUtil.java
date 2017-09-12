package cn.bforce.common.utils.string;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <p class="detail">
 * 功能：日期处理类
 * </p>
 * @ClassName: DateUtil 
 * @version V1.0  
 * @date 2017年9月7日 
 * @author yuandx
 * Copyright 2017 b-force.cn, Inc. All rights reserved
 */
public class DateUtil
{

    // 完整时间
    public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    // 年月日
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    // 年月日中文
    public static final String YYYYMMDD_CHINESE = "yyyy年MM月dd日";

    // 年月日(无下划线)
    public static final String YYYYMMDD = "yyyyMMdd";

    // 年月日时分秒(无下划线)
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    // 年月日时分秒(年份只取两位)
    public static final String YY_MMDDHHMMSS = "yyMMddHHmmss";

    // 时分秒
    public static final String HHMMSS = "HH:mm:ss";

    // 时分秒
    public static final String HHMM = "HH:mm";

    // SimpleDateFormat("HH:mm:ss");
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    // 年月日时分秒毫秒(无下划线)
    public static final String YYYYMMDDHHMMSS_S = "yyyyMMddHHmmssS";

    // 年月日
    public static final String YYMMDDHH = "yyMMddHH";

    // 月日
    public static final String MMDD = "MM-dd";

    // 年
    public static final String YYYY = "yyyy";

    // 月
    public static final String MM = "MM";

    // 日
    public static final String DD = "dd";

    private static final DateFormat getFormat(String format)
    {
        return new SimpleDateFormat(format);
    }

    /**
     * yyMMddHHmmss
     * 
     * @param date
     * @return
     */
    public static final String yyssFormat(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YY_MMDDHHMMSS).format(date);
    }

    /**
     * yyMMddHH
     * 
     * @param date
     * @return
     */
    public static final String dtPrimary(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YYMMDDHH).format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static final String simpleFormat(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YYYY_MM_DD_HHMMSS).format(date);
    }

    /**
     * yyyy-MM-dd
     * 
     * @param date
     * @return
     */
    public static final String dtSimpleFormat(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YYYY_MM_DD).format(date);
    }

    /**
     * yyyyMMddHHmmssS
     * 
     * @param date
     * @return
     */
    public static final String dtLongMillFormat(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YYYYMMDDHHMMSS_S).format(date);
    }

    /**
     * yyyy-mm-dd 日期格式转换为日期
     * 
     * @param strDate
     * @return
     */
    public static final Date strToDtSimpleFormat(String strDate)
    {
        if (strDate == null)
        {
            return null;
        }

        try
        {
            return getFormat(YYYY_MM_DD).parse(strDate);
        }
        catch (Exception e)
        {}

        return null;
    }

    /**
     * yyyy-MM-dd HH:mm 日期格式转换为日期
     * 
     * @param strDate
     * @return
     */
    public static final Date strToSimpleFormat(String strDate)
    {
        if (strDate == null)
        {
            return null;
        }

        try
        {
            return getFormat(YYYY_MM_DD_HH_MM).parse(strDate);

        }
        catch (Exception e)
        {}

        return null;
    }

    /**
     * yyyy-MM-dd HH:mm 或者yyyy-MM-dd 转换为日期格式
     * 
     * @param strDate
     * @return
     */
    public static final Date strToDate(String strDate)
    {
        if (strToSimpleFormat(strDate) != null)
        {
            return strToSimpleFormat(strDate);
        }
        else
        {
            return strToDtSimpleFormat(strDate);
        }

    }

    /**
     * 获取输入日期的相差日期
     * 
     * @param dt
     * @param idiff
     * @return
     */
    public static final String getDiffDate(Date dt, int idiff)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(dt);
        c.add(Calendar.DATE, idiff);
        return dtSimpleFormat(c.getTime());
    }

    /**
     * 获取输入日期月份的相差日期
     * 
     * @param dt
     * @param idiff
     * @return
     */
    public static final String getDiffMon(Date dt, int idiff)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, idiff);
        return dtSimpleFormat(c.getTime());
    }

    /**
     * yyyy年MM月dd日
     * 
     * @param date
     * @return
     */
    public static final String dtSimpleChineseFormat(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YYYYMMDD_CHINESE).format(date);
    }

    /**
     * yyyy-MM-dd到 yyyy年MM月dd日 转换
     * 
     * @param date
     * @return
     */
    public static final String dtSimpleChineseFormatStr(String date)
        throws ParseException
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YYYYMMDD_CHINESE).format(string2Date(date));
    }

    /**
     * yyyy-MM-dd 日期字符转换为时间
     * 
     * @param stringDate
     * @return
     * @throws ParseException
     */
    public static final Date string2Date(String stringDate)
        throws ParseException
    {
        if (stringDate == null)
        {
            return null;
        }

        return getFormat(YYYY_MM_DD).parse(stringDate);
    }

    /**
     * 返回日期时间
     * 
     * @param stringDate
     * @return
     * @throws ParseException
     */
    public static final Date string2DateTime(String stringDate)
        throws ParseException
    {
        if (StringUtils.isBlank(stringDate))
        {
            return null;
        }

        return getFormat(YYYY_MM_DD_HHMMSS).parse(stringDate);
    }

    /**
     * 计算日期差值
     * 
     * @param String
     * @param String
     * @return int（天数）
     */
    public static final int calculateDecreaseDate(String beforDate, String afterDate)
        throws ParseException
    {
        Date date1 = getFormat(YYYY_MM_DD).parse(beforDate);
        Date date2 = getFormat(YYYY_MM_DD).parse(afterDate);
        long decrease = getDateBetween(date1, date2) / 1000 / 3600 / 24;
        int dateDiff = (int)decrease;
        return dateDiff;
    }

    public static final int calculateDecreaseDate(Date beforDate, Date afterDate)
        throws ParseException
    {
        String beforDate1 = dtSimpleFormat(beforDate);
        String afterDate2 = dtSimpleFormat(afterDate);
        Date date1 = getFormat(YYYY_MM_DD).parse(beforDate1);
        Date date2 = getFormat(YYYY_MM_DD).parse(afterDate2);
        long decrease = getDateBetween(date1, date2) / 1000 / 3600 / 24;
        int dateDiff = (int)decrease;
        return dateDiff;
    }

    /**
     * 计算时间差值
     * 
     * @param String
     * @param String
     * @return int（分钟）
     */
    public static final int calculateDecreaseMinute(Date beforDate, Date afterDate)
    {
        long decrease = getDateBetween(beforDate, afterDate) / 1000 / 60;
        int dateDiff = (int)decrease;
        return dateDiff;
    }

    /**
     * 计算时间差值
     * 
     * @param String
     * @param String
     * @return int（秒）
     */
    public static final int calculateDecreaseSecond(Date beforDate, Date afterDate)
    {
        long decrease = getDateBetween(beforDate, afterDate) / 1000;
        int dateDiff = (int)decrease;
        return dateDiff;
    }

    /**
     * 计算时间差
     * 
     * @param dBefor
     *            首日
     * @param dAfter
     *            尾日
     * @return 时间差(毫秒)
     */
    public static final long getDateBetween(Date dBefor, Date dAfter)
    {
        long lBefor = 0;
        long lAfter = 0;
        long lRtn = 0;

        /** 取得距离 1970年1月1日 00:00:00 GMT 的毫秒数 */
        lBefor = dBefor.getTime();
        lAfter = dAfter.getTime();

        lRtn = lAfter - lBefor;

        return lRtn;
    }

    /**
     * 返回日期时间（Add by Gonglei）
     * 
     * @param stringDate
     *            (yyyyMMdd)
     * @return
     * @throws ParseException
     */
    public static final Date shortstring2Date(String stringDate)
        throws ParseException
    {
        if (stringDate == null)
        {
            return null;
        }

        return getFormat(YYYYMMDD).parse(stringDate);
    }

    /**
     * 返回短日期格式（yyyyMMdd格式）
     * 
     * @param stringDate
     * @return
     * @throws ParseException
     */
    public static final String shortDate(Date Date)
    {
        if (Date == null)
        {
            return null;
        }

        return getFormat(YYYYMMDD).format(Date);
    }

    /**
     * 返回长日期格式（yyyyMMddHHmmss格式）
     * 
     * @param stringDate
     * @return
     * @throws ParseException
     */
    public static final String longDate(Date Date)
    {
        if (Date == null)
        {
            return null;
        }

        return getFormat(YYYYMMDDHHMMSS).format(Date);
    }

    /**
     * yyyy-MM-dd 日期字符转换为长整形
     * 
     * @param stringDate
     * @return
     * @throws ParseException
     */
    public static final Long string2DateLong(String stringDate)
        throws ParseException
    {
        Date d = string2Date(stringDate);

        if (d == null)
        {
            return null;
        }

        return new Long(d.getTime());
    }

    /**
     * 日期转换为字符串 HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static final String hmsFormat(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(HHMMSS).format(date);
    }

    /**
     * 日期转换为字符串 HH:mm
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static final String hmFormat(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return getFormat(HHMM).format(date);
    }

    /**
     * 日期转换为字符串 MM-dd
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static final String mdFormat(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return getFormat(MMDD).format(date);
    }

    /**
     * 时间转换字符串 2005-06-30 15:50
     * 
     * @param date
     * @return
     */
    public static final String simpleDate(Date date)
    {
        if (date == null)
        {
            return "";
        }

        return getFormat(YYYY_MM_DD_HH_MM).format(date);
    }

    /**
     * 获取当前日期的日期差 now= 2005-07-19 diff = 1 -> 2005-07-20 diff = -1 -> 2005-07-18
     * 
     * @param diff
     * @return
     */
    public static final String getDiffDate(int diff)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        return dtSimpleFormat(c.getTime());
    }

    public static final Date getDiffDateTime(int diff)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        return c.getTime();
    }

    /**
     * 获取当前日期的日期时间差
     * 
     * @param diff
     * @param hours
     * @return
     */
    public static final String getDiffDateTime(int diff, int hours)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        c.add(Calendar.HOUR, hours);
        return dtSimpleFormat(c.getTime());
    }

    public static final String getDiffDate(String srcDate, String format, int diff)
    {
        DateFormat f = new SimpleDateFormat(format);

        try
        {
            Date source = f.parse(srcDate);
            Calendar c = Calendar.getInstance();

            c.setTime(source);
            c.add(Calendar.DATE, diff);
            return f.format(c.getTime());
        }
        catch (Exception e)
        {
            return srcDate;
        }
    }

    /**
     * 把日期类型的日期换成数字类型 YYYYMMDD类型
     * 
     * @param date
     * @return
     */
    public static final Long dateToNumber(Date date)
    {
        if (date == null)
        {
            return null;
        }

        Calendar c = Calendar.getInstance();

        c.setTime(date);

        String month;
        String day;

        if ((c.get(Calendar.MONTH) + 1) >= 10)
        {
            month = "" + (c.get(Calendar.MONTH) + 1);
        }
        else
        {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        if (c.get(Calendar.DATE) >= 10)
        {
            day = "" + c.get(Calendar.DATE);
        }
        else
        {
            day = "0" + c.get(Calendar.DATE);
        }

        String number = c.get(Calendar.YEAR) + "" + month + day;

        return new Long(number);
    }

    /**
     * 获取每月的某天到月末的区间
     * 
     * @param date
     * @return
     */
    public static Map<String, String> getLastWeek(String StringDate, int interval)
        throws ParseException
    {
        Map<String, String> lastWeek = new HashMap<String, String>();
        Date tempDate = DateUtil.shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);

        int dayOfMonth = cad.getActualMaximum(Calendar.DAY_OF_MONTH);

        cad.add(Calendar.DATE, (dayOfMonth - 1));
        lastWeek.put("endDate", DateUtil.shortDate(cad.getTime()));
        cad.add(Calendar.DATE, interval);
        lastWeek.put("startDate", DateUtil.shortDate(cad.getTime()));

        return lastWeek;
    }

    /**
     * 获取下月
     * 
     * @param date
     * @return
     */
    public static String getNextMon(String StringDate)
        throws ParseException
    {
        Date tempDate = DateUtil.shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.MONTH, 1);
        return DateUtil.shortDate(cad.getTime());
    }

    /**
     * add by daizhixia 20050808 获取下一天
     * 
     * @param StringDate
     * @return
     * @throws ParseException
     */
    public static String getNextDay(String StringDate)
        throws ParseException
    {
        Date tempDate = DateUtil.string2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, 1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by chencg 获取下一天 返回 dtSimple 格式字符
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getNextDay(Date date)
        throws ParseException
    {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(Calendar.DATE, 1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by shengyong 20050808 获取前一天
     * 
     * @param StringDate
     * @return
     * @throws ParseException
     */
    public static String getBeforeDay(String StringDate)
        throws ParseException
    {
        Date tempDate = DateUtil.string2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, -1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by shengyong 获取前一天 返回 dtSimple 格式字符
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getBeforeDay(Date date)
        throws ParseException
    {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(Calendar.DATE, -1);
        return DateUtil.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by chencg 获取下一天 返回 dtshort 格式字符
     * 
     * @param StringDate
     *            "20061106"
     * @return String "2006-11-07"
     * @throws ParseException
     */
    public static Date getNextDayDtShort(String StringDate)
        throws ParseException
    {
        Date tempDate = DateUtil.shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, 1);
        return cad.getTime();
    }

    /**
     * add by daizhixia 20050808 取得相差的天数
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static long countDays(String startDate, String endDate)
    {
        Date tempDate1 = null;
        Date tempDate2 = null;
        long days = 0;

        try
        {
            tempDate1 = DateUtil.string2Date(startDate);

            tempDate2 = DateUtil.string2Date(endDate);
            days = (tempDate2.getTime() - tempDate1.getTime()) / (1000 * 60 * 60 * 24);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 返回日期相差天数，向下取整数
     * 
     * @param dateStart
     *            一般前者小于后者dateEnd
     * @param dateEnd
     * @return
     */
    public static int countDays(Date dateStart, Date dateEnd)
    {
        if ((dateStart == null) || (dateEnd == null))
        {
            return -1;
        }

        return (int)((dateEnd.getTime() - dateStart.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 校验start与end相差的天数，是否满足end-start lessEqual than days
     * 
     * @param start
     * @param end
     * @param days
     * @return
     */
    public static boolean checkDays(Date start, Date end, int days)
    {
        int g = countDays(start, end);

        return g <= days;
    }

    public static Date now()
    {
        return new Date();
    }

    /**
     * alahan add 20050825 获取传入时间相差的日期
     * 
     * @param dt
     *            传入日期，可以为空
     * @param diff
     *            需要获取相隔diff天的日期 如果为正则取以后的日期，否则时间往前推
     * @return
     */
    public static String getDiffStringDate(Date dt, int diff)
    {
        Calendar ca = Calendar.getInstance();

        if (dt == null)
        {
            ca.setTime(new Date());
        }
        else
        {
            ca.setTime(dt);
        }

        ca.add(Calendar.DATE, diff);
        return dtSimpleFormat(ca.getTime());
    }

    /**
     * 校验输入的时间格式是否合法，但不需要校验时间一定要是8位的
     * 
     * @param statTime
     * @return alahan add 20050901
     */
    public static boolean checkTime(String statTime)
    {
        if (statTime.length() > 8)
        {
            return false;
        }

        String[] timeArray = statTime.split(":");

        if (timeArray.length != 3)
        {
            return false;
        }

        for (int i = 0; i < timeArray.length; i++ )
        {
            String tmpStr = timeArray[i];

            try
            {
                Integer tmpInt = new Integer(tmpStr);

                if (i == 0)
                {
                    if ((tmpInt.intValue() > 23) || (tmpInt.intValue() < 0))
                    {
                        return false;
                    }
                    else
                    {
                        continue;
                    }
                }

                if ((tmpInt.intValue() > 59) || (tmpInt.intValue() < 0))
                {
                    return false;
                }
            }
            catch (Exception e)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * 返回日期时间（Add by Gonglei）
     * 
     * @param stringDate
     *            (yyyyMMdd)
     * @return
     * @throws ParseException
     */
    public static final String StringToStringDate(String stringDate)
    {
        if (stringDate == null)
        {
            return null;
        }

        if (stringDate.length() != 8)
        {
            return null;
        }

        return stringDate.substring(0, 4) + stringDate.substring(4, 6)
               + stringDate.substring(6, 8);
    }

    /**
     * 将字符串按format格式转换为date类型
     * 
     * @param str
     * @param format
     * @return
     */
    public static Date string2Date(String str, String format)
    {

        try
        {
            if (StringUtils.isBlank(format)) return string2Date(str);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(str);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * @Description: 今年的生日日期
     * @author wanDong
     * @param birthday
     *            生日
     * @return yyyy-MM-dd
     */
    public static final String getYearBirthday(String birthday)
        throws ParseException
    {
        Date date = string2Date(birthday);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int MM = c.get(2) + 1;
        int dd = c.get(5);
        c.setTime(new Date());
        int yyyy = c.get(1);
        birthday = String.format("%04d-%02d-%02d", yyyy, MM, dd);
        return birthday;
    }

    /**
     * 加减天数
     * 
     * @param date
     * @return Date
     * @author shencb 2006-12 add
     */
    public static final Date increaseDate(Date aDate, int days)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(aDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 加减天数
     * 
     * @param date
     * @return Date
     * @author shencb 2006-12 add
     */
    public static final String increaseDate2String(Date aDate, int days)
    {
        Date date = increaseDate(aDate, days);

        return dtSimpleFormat(date);
    }

    /**
     * <p class="detail"> 功能：加减分钟数 </p>
     * 
     * @author panwuhai
     * @date 2016年4月23日
     * @param minute
     * @return
     */
    public static final Date increaseMinute(int minute)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * 把日期2007/06/14转换为20070614
     * 
     * @author Yufeng 2007
     * @method formatDateString
     * @param date
     * @return
     */
    public static String formatDateString(String date)
    {
        String result = "";
        if (StringUtils.isBlank(date))
        {
            return "";
        }
        if (date.length() == 10)
        {
            result = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
        }
        return result;
    }

    /**
     * 获得日期是周几
     * 
     * @author xiang.zhaox
     * @param date
     * @return dayOfWeek
     */
    public static int getDayOfWeek(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 将8位日期转换为10位日期（Add by Alcor）
     * 
     * @param stringDate
     *            yyyymmdd
     * @return yyyy-mm-dd
     * @throws ParseException
     */
    public static final String shortString2SimpleString(String shortString)
    {
        if (shortString == null)
        {
            return null;
        }
        try
        {
            return getFormat(YYYY_MM_DD).format(shortstring2Date(shortString));
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 功能：获取指定日期的年份
     * 
     * @author liu.weihao
     * @date 2016-10-24
     * @param date
     * @return
     */
    public static final String getYear(Date date)
    {
        if (date == null)
        {
            return "";
        }
        return getFormat(YYYY).format(date);
    }

    /**
     * 功能：获取指定日期的月份
     * 
     * @author liu.weihao
     * @date 2016-10-24
     * @param date
     * @return
     */
    public static final String getMonth(Date date)
    {
        if (date == null)
        {
            return "";
        }
        return getFormat(MM).format(date);
    }

    /**
     * 功能：获取指定日期的日
     * 
     * @author liu.weihao
     * @date 2016-10-24
     * @param date
     * @return
     */
    public static final String getDay(Date date)
    {
        if (date == null)
        {
            return "";
        }
        return getFormat(DD).format(date);
    }

    /**
     * <p class="detail"> 功能：获取两个日期的间隔月份数量，不考虑时分秒 </p>
     * 
     * @author liu.weihao
     * @date 2016-10-24
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static final int getMonthDiff(Date startDate, Date endDate)
    {
        if (startDate == null || endDate == null) return 0;
        if (startDate.getTime() >= endDate.getTime()) return 0;
        int monthDiff = 0;
        Integer startYear = Integer.valueOf(getYear(startDate));
        Integer endYear = Integer.valueOf(getYear(endDate));
        if (!startYear.equals(endYear))
        { 
            // 跨年，跨度×12
            monthDiff += (endYear - startYear) * 12;
        }
        Integer startMonth = Integer.valueOf(getMonth(startDate));
        Integer endMonth = Integer.valueOf(getMonth(endDate));
        monthDiff += endMonth - startMonth;

        if (endMonth >= startMonth
            && Integer.valueOf(getDay(startDate)) > Integer.valueOf(getDay(endDate)))
        {
            monthDiff-- ;
        }
        return monthDiff;
    }

}
