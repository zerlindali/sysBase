package cn.bforce.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class ExDateUtils {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");	
	private static DateFormat shortDateTimeFormat = new SimpleDateFormat("yyyy-M-d HH:mm");	
	private static DateFormat shortDayTimeFormat = new SimpleDateFormat("M-d HH:mm");
	private static DateFormat shortTimeFormat = new SimpleDateFormat("HH:mm");	
	private static DateFormat shortDateFormat = new SimpleDateFormat("MM-dd");
	private static DateFormat yearMonthDateFormat = new SimpleDateFormat("yyyy-MM");
	private static DateFormat periodDateFormat = new SimpleDateFormat("yyyyMM");
	
	
	public static String getToday() {
		Date currentDate = new Date();
		return dateFormat.format(currentDate);
	}

	public static String getLastMonth() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return dateFormat.format(calendar.getTime());
	}

	public static String formatYearMonth(Date date) {

		if (date == null || yearMonthDateFormat == null) return null;
		
		return yearMonthDateFormat.format(date);
		
	}
	
	public static String formatPeriod(Date date) {

		if (date == null || periodDateFormat == null) return null;
		
		return periodDateFormat.format(date);
	}
	
	public static String formatShortDate(Date date) {

		if (date == null || shortDateFormat == null) return null;
		
		return shortDateFormat.format(date);
	}
	
	public static String formatDate(Date date) {

		if (date == null || dateFormat == null)	return null;
		
		return dateFormat.format(date);
	}

	public static String formatDateTime(Date date) {

		if (date == null || dateTimeFormat == null)	return null;

		return dateTimeFormat.format(date);
	}

	public static String formatShortDateTime(Date date) {

		if (date == null || shortDateTimeFormat == null)	return null;

		return shortDateTimeFormat.format(date);
	}
	
	public static String formatShortDayTime(Date date) {

		if (date == null || shortDayTimeFormat == null)	return null;

		return shortDayTimeFormat.format(date);
	}
	
	public static String formatTime(Date date) {

		if (date == null || timeFormat == null)	return null;

		return timeFormat.format(date);
	}

	public static String formatShortTime(Date date) {

		if (date == null || shortTimeFormat == null)	return null;

		return shortTimeFormat.format(date);
	}
	
	public static String formatDateOrTime(Date date) {

		if (date == null || timeFormat == null)	return null;
		
		Date today = new Date();
		
		if (getMonth(date) == getMonth(today) && getMonthDay(date) == getMonthDay(today)){
			return shortTimeFormat.format(date);
		}
		return shortDateFormat.format(date);
	}

	
	public static String format(Date date, String pattern) {

		if (date == null || pattern == null) return null;	
		
		DateFormat tmpDateFormat = new SimpleDateFormat(pattern);
		return tmpDateFormat.format(date);
	}
	public static Date getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	
	public static Date getSysDateTime() {
		return new Date();
	}

	public static Date getBeginningOfWeek(Date beginDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(beginDate);
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		while (dayOfWeek > calendar.getFirstDayOfWeek()){
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		}
		if (dayOfWeek < calendar.getFirstDayOfWeek()){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	
	public static Date getBeginningOfWeek() {
		return getBeginningOfWeek(new Date());
	}

	public static Date getEndOfWeek(Date aDate) {
		Date tmpDate = getBeginningOfWeek(aDate);
		tmpDate = addDay(tmpDate, 6);
		return tmpDate;
	}
	
	public static Date getEndOfWeek() {
		Date tmpDate = getBeginningOfWeek();
		tmpDate = addDay(tmpDate, 6);
		return tmpDate;
	}
	
	public static Date getBeginningOfYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH , 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getBeginningOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH , 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);		
		return calendar.getTime();
	}
	
	public static Date getBeginningOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getEndOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date getEndOfMonth(int month){
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
	
	public static String getEndOfMonth(String startDate) {
		Date sDate = parseDate(startDate);
		if (sDate == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return formatDate(calendar.getTime());
	}

	public static Date parseDate(String dateStr, String datePattern) {
		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static Date parseDate(String dateStr) {
		
		DateFormat dateFormat = null;
		
		if (dateStr.indexOf("CST") >= 0){
			dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);			
		}else if (dateStr.indexOf("CDT") >= 0){
			dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		}else if (dateStr.indexOf("UTC") >= 0){
			dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'UTC'Z yyyy", Locale.US);
		}else if (dateStr.indexOf("GMT") >= 0){
			dateStr = dateStr.replace("GMT+0800", "GMT+08:00");
			dateFormat = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss z", Locale.US);
		}else if (dateStr.indexOf("-") < 0 && dateStr.length() == 8){
			dateFormat = new SimpleDateFormat("yyyyMMdd");
		}else if (dateStr.indexOf(":") > 0 && dateStr.indexOf("-") > 0 && dateStr.length() == 19){
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else if (dateStr.indexOf(":") > 0 && dateStr.indexOf("-") > 0 && dateStr.length() == 16){
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}else if (dateStr.indexOf(":") > 0 && dateStr.indexOf("/") > 0 && dateStr.length() == 19){
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}else if (dateStr.indexOf(":") > 0 && dateStr.indexOf("/") > 0 && dateStr.length() == 16){
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		}else if (dateStr.indexOf("/") > 0 && dateStr.length()>= 8 && dateStr.length() <= 10){
			dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		}else{
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		}
				
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static Date parseDate(long time) {
		
		if (time > 0){
			Date tmpDate = new Date();
			tmpDate.setTime(time);
			return tmpDate;
		}
		
		return null;
	}
	
	public static int getYear(Date sDate){
		if (sDate == null){
			sDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		return calendar.get(Calendar.YEAR);
	}
	
	public static int getMonth(Date sDate){
		if (sDate == null){
			sDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		return (calendar.get(Calendar.MONTH) + 1);
	}
	
	public static int getMonthDay(Date sDate){
		if (sDate == null){
			sDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		return (calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	
	public static int getWeekDay(Date sDate){
		if (sDate == null){
			sDate = new Date();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		weekDay = weekDay -1;
		if (weekDay == 0){
			weekDay = 7;
		}
		
		return weekDay;
		
	}
	
	public static int getHourOfDay(Date sDate){
		if (sDate == null){
			sDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		return (calendar.get(Calendar.HOUR_OF_DAY));
	}
	
	public static int getMinute(Date sDate){
		if (sDate == null){
			sDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		return (calendar.get(Calendar.MINUTE));
	}
	
	public static String getZhWeekDay(Date sDate){
		
		int weekDay = getWeekDay(sDate);
		
		if (weekDay == 1){
			return "一";
		}else if (weekDay == 2){
			return "二";
		}else if (weekDay == 3){
			return "三";
		}else if (weekDay == 4){
			return "四";
		}else if (weekDay == 5){
			return "五";
		}else if (weekDay == 6){
			return "六";
		}else if (weekDay == 7){
			return "日";
		}
		
		return null;
	}
	
	public static Date addMonth(Date date, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);		
		return calendar.getTime();
		
	}
	
	public static Date addDay(Date date, int days) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);		
		return calendar.getTime();
		
	}
	
	public static Date addMinute(Date date, int minutes) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);		
		return calendar.getTime();
		
	}
	
	public static DateFormat getDateFormatter(){
		return dateFormat;
	}
	
	public static DateFormat getDateTimeFormatter(){
		return dateTimeFormat;
	}
	
	public static DateFormat getShortDateFormatter(){
		return shortDateFormat;
	}
	
	
	public static DateFormat getTimeFormatter(){
		return timeFormat;
	}
	
	public static DateFormat getShortDateTimeFormater(){
		return shortDateTimeFormat;
	}
	
	public static boolean isBefore(Date leftDate, Date rightDate){
		return leftDate.before(rightDate);
	}
	
	public static boolean isAfter(Date leftDate, Date rightDate){
		return leftDate.after(rightDate);
	}
	
	public static void main(String[] args) {
		
		Date today = new Date();
		
		
		System.out.println(today);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println(dateFormat.format(today));
		
		
		dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);  
				
		System.out.println(dateFormat.format(today));
		
		dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'UTC'Z yyyy", Locale.US);
		System.out.println(dateFormat.format(today));
		
//		Date f = ExDateUtils.getBeginningOfWeek();
//		System.out.println("aaaaa:" + ExDateUtils.formatDate(f));
//		
//		Date e = ExDateUtils.getEndOfWeek();
//		System.out.println("bbbb:" + ExDateUtils.formatDate(e));
		
		Date g = ExDateUtils.getBeginningOfWeek(parseDate("2011-5-7"));
		System.out.println("ccc:" + ExDateUtils.formatDate(g));
		
		g = ExDateUtils.getBeginningOfWeek(parseDate("2011-5-8"));
		System.out.println("ccc:" + ExDateUtils.formatDate(g));
		
		g = ExDateUtils.getBeginningOfWeek(parseDate("2011-5-9"));
		System.out.println("ccc:" + ExDateUtils.formatDate(g));
		
	}
	
	public static Date setTime(Date date, String time){
		Date tmpDate = date;
		if (date != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(tmpDate);
			String[] strArr = ExStringUtils.split(time, ":");
			int hour = 0, minute = 0, second = 0;
			if (strArr != null && strArr.length == 2){
				hour = Integer.parseInt(strArr[0]);
				minute  = Integer.parseInt(strArr[1]);
			}else if (strArr != null && strArr.length == 3){
				hour = Integer.parseInt(strArr[0]);
				minute  = Integer.parseInt(strArr[1]);
				second  = Integer.parseInt(strArr[2]);
			}
			
			calendar.set(Calendar.HOUR, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			calendar.set(Calendar.MILLISECOND, 0);
			tmpDate = calendar.getTime();
			date = calendar.getTime();
		}
		
		return tmpDate;
	}
	
	
    public static int getWeekNum(Date date){    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	return calendar.get(Calendar.WEEK_OF_YEAR);
    	
    }
    
    /**
     * 获得日期相差的天数
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static int getDiffDay(Date startTime, Date endTime) {
		
		return (int)dateDiff(startTime, endTime)[0];
	}

	/**
	 * 返回long[] times = { 天, 小时, 分钟, 秒 };
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */
	public static long[] dateDiff(Date startTime, Date endTime) {

		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数

		long nh = 1000 * 60 * 60;// 一小时的毫秒数

		long nm = 1000 * 60;// 一分钟的毫秒数

		long ns = 1000;// 一秒钟的毫秒数

		long diff;

		// 获得两个时间的毫秒时间差异

		diff = endTime.getTime() - startTime.getTime();

		long day = diff / nd;// 计算差多少天

		long hour = diff % nd / nh;// 计算差多少小时

		long min = diff % nd % nh / nm;// 计算差多少分钟

		long sec = diff % nd % nh % nm / ns;// 计算差多少秒

		long[] times = { day, hour, min, sec };
		
		return times;
	}
	
	/**
	 * 
	 * @param timeout
	 */
	public static void sleep(long timeout){
		
		try {
			TimeUnit.MILLISECONDS.sleep(timeout);
		} catch (InterruptedException e) {
			//
		}
		
	}
}
