/**
 * @Title DateUtil
 * @date 2015年4月17日 下午5:10:31
 * @author 赵忠红
 * @Description 日期时间工具类
 */
package com.isoftstone.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;



public class DateUtil {
	/**
	 * 时间格式
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_DATETIME_MINTIME = "yyyy-MM-dd HH:mm";
	public static final String DEFAULT_DATE_FORMAT_CH = "yyyy年MM月dd日";
	public static final String DEFAULT_DATETIME_FORMAT_CH = "yyyy年MM月dd日 HH时mm分ss秒";

	public static final String DEFAULT_DATE_FORMAT_SHT = "yyyyMMdd";
	public static final String DEFAULT_DATETIME_FORMAT_SHT = "yyyyMMddHHmmss";
	public static final String DEFAULT_DATETIME_MINTIME_SHT = "yyyyMMddHHmm";
	//格式化到毫秒
	public static final String DEFAULT_DATETIME_FORMAT_MILL = "yyyyMMddHHmmssSSS";
	// 格式为：2015-04-21 星期二
	public static final String DEFAULT_DATETIME_FORMAT_WEEK = "yyyy-MM-dd E";

	/**
	 * 一天中开始时间和结束时间
	 */
	public static final String DAY_START_TIME = "00:00:00";
	public static final String DAY_END_TIME = "23:59:59";

	/**
	 * 私有构造方法,禁止创建实例
	 */
	private DateUtil() {

	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 格式化当前系统时间 格式为：yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getDate() {
		return getDateTime(DEFAULT_DATE_FORMAT);
	}

	/**
	 * 格式化当前系统时间 格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getDateTime() {
		return getDateTime(DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * 获取当前系统时间,并格式化为指定的格式
	 * 
	 * @param pattern
	 *            日期显示的格式
	 * @return
	 */
	public static String getDateTime(String pattern) {
		Date dateTime = Calendar.getInstance().getTime();
		return getDateTime(dateTime, pattern);
	}

	/**
	 * 将时间格式化为指定的格式
	 * 
	 * @param date
	 *            需要进行格式化的日期
	 * @param pattern
	 *            显示格式
	 * @return 日期时间字符串
	 */
	public static String getDateTime(Date date, String pattern) {
		if (StringUtils.isEmpty(pattern)) {
			pattern = DEFAULT_DATETIME_FORMAT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 获取某周的周一 时间格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param week
	 *            -1上周一,0本周一,1下周一,依次类推
	 * @return
	 */
	public static Timestamp getMonday(int week) {
		// 以往:-1(上上周日的时间) 上周:1   本周:2    下周:3   下周以后(下下周一的时间):-2
		switch (week) {
		case -1:
			week = -2;
			break;
		case 1:
			week = -1;
			break;
		case 2:
			week = 0;
			break;
		case 3:
			week = 1;
			break;
		case -2:
			week = 2;
			break;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, week * 7);
		// 想周几,这里就传几Calendar.MONDAY(TUESDAY...)
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String dateTime = getDateTime(cal.getTime(), DEFAULT_DATE_FORMAT);
		return stringToTimestamp(getStartDateTimeByDay(dateTime));
	}

	/**
	 * 获取某周的周日 时间格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param week
	 *            0上周日,1 本周日,2 下周日,依次类推
	 * @return
	 */
	public static Timestamp getSunday(int week) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getMonday(week).getTime());
		cal.add(Calendar.DATE, 6);
		String dateTime = getDateTime(cal.getTime(), DEFAULT_DATE_FORMAT);
		return stringToTimestamp(getEndDateTimeByDay(dateTime));
	}

	/**
	 * 获取系统当前毫秒
	 * 
	 * @return
	 */
	public static long getTimeStamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 根据指定日期获取毫秒数
	 * 
	 * @param date
	 *            当前日期
	 * @return
	 */
	public static long getTimeStamp(Date date) {
		return date.getTime();
	}

	/**
	 * 指定字符日期与格式获取系统当前毫秒
	 * 
	 * @param dateStr
	 *            字符日期
	 * @param pattern
	 *            日期格式
	 * @return 出错时返回系统当前毫秒，未出错则进行正常转换
	 */
	public static long getTimeStamp(String dateStr, String pattern) {
		if (StringUtils.isEmpty(dateStr))
			return System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date == null ? System.currentTimeMillis() : date.getTime();
	}
	/**
	 * 将字符串形式的时间格式化为另外一种格式的字符串时间
	 * @param dataStr
	 * @param sourcePatternStr
	 * @param targetPatternStr
	 * @return
	 */
	public static String getDataStrFromStr(String dateStr,String sourcePatternStr, String targetPatternStr){
		Long timeLong = getTimeStamp(dateStr, sourcePatternStr);
		return getDateTime(new Date(timeLong), targetPatternStr);
	}

	/**
	 * 得到当前年份
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 得到当前月份
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		// 用get得到的月份数比实际的小1，需要加上
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到当前日
	 * 
	 * @return
	 */
	public static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
	 * 
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(int days) {
		return add(getNow(), days, Calendar.DATE);
	}

	/**
	 * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date
	 *            基准日期
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(Date date, int days) {
		return add(date, days, Calendar.DATE);
	}

	/**
	 * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
	 * 
	 * @param months
	 *            增加的月份数
	 * @return 增加以后的日期
	 */
	public static Date addMonths(int months) {
		return add(getNow(), months, Calendar.MONTH);
	}

	/**
	 * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
	 * 
	 * @param date
	 *            基准日期
	 * @param months
	 *            增加的月份数
	 * @return 增加以后的日期
	 */
	public static Date addMonths(Date date, int months) {
		return add(date, months, Calendar.MONTH);
	}

	/**
	 * 内部方法。为指定日期增加相应的天数或月数
	 * 
	 * @param date
	 *            基准日期
	 * @param amount
	 *            增加的数量
	 * @param field
	 *            增加的单位，年，月或者日
	 * @return 增加以后的日期
	 */
	private static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(field, amount);

		return calendar.getTime();
	}

	/**
	 * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差天数
	 */
	public static long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差月份数
	 */
	public static int diffMonths(Date one, Date two) {

		Calendar calendar = Calendar.getInstance();
		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int yearOne = calendar.get(Calendar.YEAR);
		int monthOne = calendar.get(Calendar.MONDAY);

		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		int yearTwo = calendar.get(Calendar.YEAR);
		int monthTwo = calendar.get(Calendar.MONDAY);

		return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	}

	/**
	 * 将一个字符串用给定的格式转换为日期类型。 <br>
	 * 注意：如果返回null，则表示解析失败
	 * 
	 * @param datestr
	 *            需要解析的日期字符串
	 * @param pattern
	 *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 解析后的日期
	 */
	public static Date parse(String datestr, String pattern) throws Exception {
		Date date = null;

		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATE_FORMAT;
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		return date;
	}

	/**
	 * 返回本月的最后一天
	 * 
	 * @return 本月最后一天的日期
	 */
	public static Date getMonthLastDay() {
		return getMonthLastDay(getNow());
	}

	/**
	 * 返回给定日期中的月份中的最后一天
	 * 
	 * @param date
	 *            基准日期
	 * @return 该月最后一天的日期
	 */
	public static Date getMonthLastDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1, 1);

		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.MILLISECOND, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 将Timestamp格式（yyyy-MM-dd HH:mm:ss）的时间转为String
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTimestamp(Timestamp time) {
		return formatTimestamp(time, DateUtil.DEFAULT_DATETIME_FORMAT);
	}
	
	public static String format(Date time) {
		return format(time, DateUtil.DEFAULT_DATETIME_FORMAT);
	}
	
	public static String format(Date time, String pattern) {
		String result = "";
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		Date current = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		result = format.format(current);

		return result;
	}
	/**
	 * 将Timestamp按指定格式的时间转为String
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String formatTimestamp(Timestamp time, String pattern) {
		String result = "";

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		Date current = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		result = format.format(current);

		return result;
	}

	/**
	 * 把时间字符串转换成timestamp
	 * 
	 * @param time
	 * @return
	 */
	public static Timestamp stringToTimestamp(String time) {
		return Timestamp.valueOf(time);
	}

	/**
	 * 取得指定日期所在一年中的第几周
	 * 
	 * @param date
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static int getSpecifiedNumForDate(String date) {
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			calendar.setTime(df.parse(date));
			calendar.setFirstDayOfWeek(Calendar.FRIDAY);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 根据日期字符串（yyyyMMdd）计算年龄
	 * 
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String getAge(String dateStr) {
		return getAge(dateStr, DEFAULT_DATE_FORMAT_SHT);
	}

	/**
	 * 根据日期字符串计算年龄
	 * 
	 * @param dateStr
	 * @param pattern
	 *            （例如：yyyyMMdd）
	 * @return
	 */
	public static String getAge(String dateStr, String pattern) {
		int age = 0;
		try {
			Date birthday = DateUtil.parse(dateStr, pattern);

			Calendar cal = Calendar.getInstance();

			if (cal.before(birthday)) {
				return "0";
				// throw new IllegalArgumentException(
				// "The birthDay is before Now.It's unbelievable!");
			}

			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(birthday);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			age = yearNow - yearBirth;

			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					// monthNow==monthBirth
					if (dayOfMonthNow <= dayOfMonthBirth) {
						age--;
					}
				} else {
					// monthNow<monthBirth
					age--;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return age + "";
	}

	/**
	 * 拼装某一天的开始时间
	 * 
	 * @param day
	 *            (yyyy-MM-dd)
	 * @return day(yyyy-MM-dd) 00:00:00
	 */
	public static String getStartDateTimeByDay(String day) {
		return day + " " + DAY_START_TIME;
	}

	/**
	 * 拼装某一天的结束时间
	 * 
	 * @param day
	 *            (yyyy-MM-dd)
	 * @return day(yyyy-MM-dd) 23:59:59
	 */
	public static String getEndDateTimeByDay(String day) {
		return day + " " + DAY_END_TIME;
	}

	public static void main(String[] args) {
		
		System.out.println(getDataStrFromStr("2018年11月21日 05时07分26秒 GMT+08:00".replace(" GMT+08:00", ""), "yyyy年MM月dd日 HH时mm分ss秒", "yyyy-MM-dd HH:mm:ss"));
		System.out.println(getDataStrFromStr("09/18/2018 13:45:59", "MM/dd/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss"));
		/*System.out.println("当前系统时间：" + getNow());
		System.out.println("将时间格式化为指定的格式： " + getDateTime(new Date(), DEFAULT_DATETIME_FORMAT_CH));
		System.out.println("获取当前系统时间,并格式化为指定的格式： " + getDateTime(DEFAULT_DATE_FORMAT_CH));
		System.out.println("格式化当前系统时间 格式为：yyyy-MM-dd： " + getDate());
		System.out.println("格式化当前系统时间 格式为：yyyy-MM-dd HH:mm:ss: " + getDateTime());
		System.out.println("获取系统当前毫秒: " + getTimeStamp());
		System.out.println("根据指定日期获取毫秒数: " + getTimeStamp(new Date()));
		System.out.println("指定字符日期与格式获取系统当前毫秒: " + getTimeStamp("1970-02-02", DEFAULT_DATE_FORMAT));
		System.out.println("当前年份: " + getCurrentYear());
		System.out.println("当前月份: " + getCurrentMonth());
		System.out.println("得到当前日: " + getCurrentDay());
		System.out.println("两天以后: " + addDays(2));
		System.out.println("月份: " + addMonths(2));
		System.out.println("本月最后一天: " + getMonthLastDay());
		System.out.println("第几周: " + getSpecifiedNumForDate("2015-4-21 00:00:00"));
		System.out.println("年龄: " + getAge("20140821"));

		// 以往:-1(上上周日的时间) 上周:1      本周:2       下周:3       下周以后(下下周一的时间):-2
		System.out.println("本周一: " + getMonday(2));
		System.out.println("下周一: " + getMonday(3));
		System.out.println("上周一: " + getMonday(1));
		System.out.println("以后: " + getMonday(-2) + "\n");

		
		System.out.println("本周日: " + getSunday(2));
		System.out.println("下周日: " + getSunday(3));
		System.out.println("上周日: " + getSunday(1));
		System.out.println("以往: " + getSunday(-1) + "\n");
		
		Date date = new Date();
		Timestamp nousedate = new Timestamp(date.getTime());

		System.out.println("周几: " + formatTimestamp(nousedate, DEFAULT_DATETIME_FORMAT_WEEK));
		
		
		System.out.println(addDays(new Date(), 365));*/
	}
}
