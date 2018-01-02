package cn.simazilin.demo.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DateUtils {

    static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static final String PATTERN_DATE_1 = "yyyy-MM-dd";

    public static final String PARTTERN_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String PARTTERN_2 = "yyyy-MM-dd HH:mm";

    public static final String PARTTERN_3 = "yyyy/MM/dd HH:mm";

    public static final String PARTTERN_4 = "yyyyMMddHHmmss";

    public static final String PARTTERN_5 = "yyyyMMddHHmmssSSS";

    public static final String PATTERN_DATE_3 = "MM月dd日";

    public static final String PATTERN_MONTH_1 = "yyyy-MM";

    public static final String PATTERN_WEEK_1 = "yyyy-ww";

    private static final Date originDate = new Date(100);

    private static char[] currentTimeFlow;

    /**
     * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static boolean isWorkday(Date date) {
        // String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
        // "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        if (w > 0 && w < 6) {
            return true;
        }
        return false;
    }

    // 拿到今天 00:00:00 的时间times long值
    public static long getTodayBeginTimes() {
        return DateUtils.parseToDate(DateUtils.formatByDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime();
    }

    // 拿到今天 00:00:00的时间date
    public static String getToday() {
        return DateUtils.formatByDate(new Date(), "yyyy-MM-dd");
    }

    // 时间 按指定格式输出 字符串
    public static String formatByDate(Date time, String parttern) {
        return new SimpleDateFormat(parttern).format(time);
        // return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(time);

    }

    // 字符串 按照指定格式 输出为日期对象
    public static Date parseToDate(String date, String parttern) {
        try {
            return new SimpleDateFormat(parttern).parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            logger.error("日期转换异常..........");
        }
        return null;
        // return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(date);
    }

    public static Date now() {
        return new Date();
    }

    /**
     * 格式化时间为fmt形式
     *
     * @param date
     * @param fmt
     * @return
     */
    public static Date parseDate(String date, String fmt) {
        try {
            return new SimpleDateFormat(fmt).parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把时间装成指定的字符串
     *
     * @param datePattern 转换格式
     * @param date        日期
     * @return 时间字符串
     */
    public static final String parseDate(String datePattern, Date date) {
        String returnValue = null;
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(datePattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 格式化时间为fmt形式
     *
     * @param date
     * @param fmt
     * @return
     */
    public static String formatDate(Date date, String fmt) {
        try {
            return new SimpleDateFormat(fmt).format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期1是否在日期2之前(只比较日期)
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean beforeDate(Date date1, Date date2) {
        return before(date1, date2, PATTERN_DATE_1);
    }

    /**
     * 日期1是否在日期2之前
     *
     * @param date1 比较日期1
     * @param date2 比较日期2
     * @param frmt  日期格式
     * @return
     */
    public static boolean before(Date date1, Date date2, String frmt) {
        date1 = trunc(date1, frmt);
        date2 = trunc(date2, frmt);
        return date1.before(date2);
    }

    /**
     * 截取日期，失败抛出异常
     *
     * @param date
     * @param frmt 日期格式
     * @return
     */
    public static Date trunc(Date date, String frmt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(frmt);
            return sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回两个日期相差的天数,有一个时间为null返回-1
     *
     * @param d1 长的时间
     * @param d2 短的时间
     * @return int
     */
    public static int diff_in_date(Date d1, Date d2) {

        if (null == d1 || null == d2) {
            return -1;
        }
        return (int) ((d1.getTime() - d2.getTime()) / 86400000);
    }

    /**
     * 返回两个日期相差的月份,有一个时间为null返回-1 结果为自然月差距,不是绝对的时间差值
     *
     * @param d1 短的时间
     * @param d2 长的时间
     * @return int
     */
    public static int diff_in_month(Date d1, Date d2) {
        if (null == d1 || null == d2) {
            return -1;
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        int c1year = c1.get(Calendar.YEAR);
        int c1month = c1.get(Calendar.MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        int c2year = c2.get(Calendar.YEAR);
        int c2month = c2.get(Calendar.MONTH);
        return (c2year - c1year) * 12 + (c2month - c1month);
    }

    /**
     * 返回两个日期相差的月份,有一个时间为null返回-1 结果为自然周差距,不是绝对的时间差值
     *
     * @param d1 短的时间
     * @param d2 长的时间
     * @return int
     */
    public static int diff_in_week(Date d1, Date d2) {
        if (null == d1 || null == d2) {
            return -1;
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        int c1year = c1.get(Calendar.YEAR);
        int c1week = c1.get(Calendar.WEEK_OF_YEAR);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        int c2year = c2.get(Calendar.YEAR);
        int c2week = c2.get(Calendar.WEEK_OF_YEAR);
        return (c2year - c1year) * 52 + (c2week - c1week);
    }

    /**
     * 给日期加天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * 给日期加月份
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * 给日期加月份
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addWeeks(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_YEAR, months);
        return c.getTime();
    }

    /**
     * 获取指定日期之前指定天,包含传入的那一天
     *
     * @param date 指定的日期
     * @param days 指定的天数
     * @return
     */
    public static String getDaysBefore(Date date, int days) {
        Date td = addDays(date, -days);
        return parseDate(PATTERN_DATE_1, td);
    }

    /**
     * 获取指定日期之前指定天,包含传入的那一天
     *
     * @param dateStr 指定的日期的字符串格式
     * @param days    指定的天数
     * @return
     */
    public static String getDaysBefore(String dateStr, int days) {
        Date date = parseDate(dateStr, PATTERN_DATE_1);
        return getDaysBefore(date, days);
    }

    /**
     * 获取指定日期之前指定天的数组,包含传入的那一天
     *
     * @param date 指定的日期
     * @param days 指定的天数
     * @return
     */
    public static List<String> getDaysBeforeArray(Date date, int days) {
        List<String> resultList = new ArrayList<String>();
        for (int i = days - 1; i >= 0; i--) {
            resultList.add(getDaysBefore(date, i));
        }
        return resultList;
    }

    /**
     * 获取指定日期之前指定天的数组,包含传入的那一天
     *
     * @param dateStr 指定的日期的字符串格式
     * @param days    指定的天数
     * @return
     */
    public static List<String> getDaysBeforeArray(String dateStr, int days) {
        List<String> resultList = new ArrayList<String>();
        for (int i = days - 1; i >= 0; i--) {
            resultList.add(getDaysBefore(dateStr, i));
        }
        return resultList;
    }

    /**
     * 获取指定日期之前指定天,包含传入的那一天
     *
     * @param date 指定的日期
     * @param days 指定的天数
     * @return
     */
    public static String getDaysAfter(Date date, int days) {
        Date td = addDays(date, days);
        return formatDate(td, PATTERN_DATE_1);
    }

    /**
     * 获取指定日期之前指定天,包含传入的那一天
     *
     * @param dateStr 指定的日期的字符串格式
     * @param days    指定的天数
     * @return
     */
    public static String getDaysAfter(String dateStr, int days) {
        Date date = parseDate(dateStr, PATTERN_DATE_1);
        return getDaysAfter(date, days);
    }

    /**
     * 获取指定日期之后指定天的数组,包含传入的那一天
     *
     * @param date 指定的日期
     * @param days 指定的天数
     * @return
     */
    public static List<String> getDaysAfterArray(Date date, int days) {
        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < days; i++) {
            resultList.add(getDaysAfter(date, i));
        }
        return resultList;
    }

    /**
     * 获取指定日期之后指定天的数组,包含传入的那一天
     *
     * @param dateStr 指定的日期的字符串格式
     * @param days    指定的天数
     * @return
     */
    public static List<String> getDaysAfterArray(String dateStr, int days) {
        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < days; i++) {
            resultList.add(getDaysAfter(dateStr, i));
        }
        return resultList;
    }

    /**
     * 获取指定日期之后指定天的数组,包含传入的那一天
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static List<String> getDaysAfterArray(Date beginDate, Date endDate) {
        List<String> resultList = new ArrayList<String>();

        int days = diff_in_date(endDate, beginDate);

        for (int i = 0, length = Math.abs(days); i < length; i++) {
            if (days > 0) {
                resultList.add(getDaysAfter(beginDate, i));
            } else {
                resultList.add(getDaysAfter(endDate, i));
            }
        }
        return resultList;
    }

    /**
     * 获取指定日期之后指定天的数组,包含传入的那一天
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static List<String> getDaysAfterArray(String beginDate, String endDate) {
        return getDaysAfterArray(parseDate(beginDate, PATTERN_DATE_1), parseDate(endDate, PATTERN_DATE_1));
    }


    /**
     * 获取指定日期之后指定天的数组,包含传入的那一天
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static List<String> getDaysBeforeArray(Date beginDate, Date endDate) {
        List<String> resultList = new ArrayList<String>();

        int days = diff_in_date(beginDate, endDate);
        for (int i = Math.abs(days); i > 0; i--) {
            if (days > 0) {
                resultList.add(getDaysBefore(beginDate, i));
            } else {
                resultList.add(getDaysBefore(endDate, i));
            }
        }
        return resultList;
    }

    /**
     * 获取指定日期之后指定天的数组,包含传入的那一天
     *
     * @param beginDate 指定的日期的字符串格式
     * @param endDate   指定的天数
     * @return
     */
    public static List<String> getDaysBeforeArray(String beginDate, String endDate) {
        return getDaysBeforeArray(parseDate(beginDate, PATTERN_DATE_1), parseDate(endDate, PATTERN_DATE_1));
    }

    public static String formatDateToDayTwo(Date date) {
        String dateFormat = "";
        try {
            dateFormat = new SimpleDateFormat("yyyyMMdd").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

    public static String getMonthsBefore(Date date, int months) {
        Date td = addMonths(date, -months);
        return parseDate(PATTERN_MONTH_1, td);
    }

    public static String getMonthsBefore(String dateStr, int months) {
        Date date = parseDate(dateStr, PATTERN_DATE_1);
        return getMonthsBefore(date, months);
    }

    public static List<String> getMonthsBeforeArray(Date date, int months) {
        List<String> resultList = new ArrayList<String>();
        for (int i = months - 1; i >= 0; i--) {
            resultList.add(getMonthsBefore(date, i));
        }
        return resultList;
    }

    public static List<String> getMonthsBeforeArray(String dateStr, int months) {
        List<String> resultList = new ArrayList<String>();
        for (int i = months - 1; i >= 0; i--) {
            resultList.add(getMonthsBefore(dateStr, i));
        }
        return resultList;
    }

    public static List<String> getMonthsBeforeArray(Date beginDate, Date endDate) {
        if (!DateUtils.beforeDate(beginDate, endDate)) {
            return new ArrayList<String>(0);
        }
        List<String> resultList = new ArrayList<String>();
        int months = diff_in_month(beginDate, endDate);
        for (int i = months - 1; i >= 0; i--) {
            resultList.add(getMonthsBefore(endDate, i));
        }
        return resultList;
    }

    public static List<String> getMonthsBeforeArray(String beginDateStr, String endDateStr) {
        return getMonthsBeforeArray(parseDate(beginDateStr, PATTERN_MONTH_1), parseDate(endDateStr, PATTERN_MONTH_1));
    }

    public static String getWeek(Date date) {
        return parseDate(PATTERN_WEEK_1, date);
    }

    public static String getWeeksBefore(Date date, int weeks) {
        Date td = addWeeks(date, -weeks);
        return parseDate(PATTERN_WEEK_1, td);
    }

    public static String getWeeksBefore(String dateStr, int weeks) {
        Date date = parseDate(dateStr, PATTERN_DATE_1);
        return getWeeksBefore(date, weeks);
    }

    public static List<String> getWeeksBeforeArray(Date date, int weeks) {
        List<String> resultList = new ArrayList<String>();
        for (int i = weeks - 1; i >= 0; i--) {
            resultList.add(getWeeksBefore(date, i));
        }
        return resultList;
    }

    public static List<String> getWeeksBeforeArray(String dateStr, int weeks) {
        List<String> resultList = new ArrayList<String>();
        for (int i = weeks - 1; i >= 0; i--) {
            resultList.add(getWeeksBefore(dateStr, i));
        }
        return resultList;
    }

    public static List<String> getWeeksBeforeArray(Date beginDate, Date endDate) {
        if (!DateUtils.beforeDate(beginDate, endDate)) {
            return new ArrayList<String>(0);
        }
        List<String> resultList = new ArrayList<String>();
        int months = diff_in_month(beginDate, endDate);
        for (int i = months - 1; i >= 0; i--) {
            resultList.add(getWeeksBefore(endDate, i));
        }
        return resultList;
    }

    public static List<String> getWeeksBeforeArray(String beginDateStr, String endDateStr) {
        return getWeeksBeforeArray(parseDate(beginDateStr, PATTERN_MONTH_1), parseDate(endDateStr, PATTERN_MONTH_1));
    }


//	public static String getNowTimeGreet(){
//		String greet = "";
//		Calendar c = Calendar.getInstance();
//		int hours = c.get(Calendar.HOUR_OF_DAY);
//		if(hours>=6 && hours < 11){
//			greet = MessageConstants.MESSAGE_MORNING;
//		}else if (hours>=11 && hours<13){
//			greet = MessageConstants.MESSAGE_NOON;
//		}else if(hours>=13 && hours<19){
//			greet = MessageConstants.MESSAGE_AFTERNOON;
//		}else if(hours>=19 && hours<24){
//			greet = MessageConstants.MESSAGE_EVENING;
//		}
//		return greet;
//	}


    //给定一个时间 判断 这个时间 是否距离 当前时间 在1小时内 决定 是否给当前用户 发送问候消息 时间字符串格式 yyyy-MM-dd hh:mm:ss
    public static Boolean duringOneHour(String datetime) {
        Boolean result = false;
        Date parseDate = DateUtils.parseDate(datetime, "yyyy-MM-dd HH:mm:ss");
        //只判断过去的时间 是否 在一小时内
        if (parseDate.getTime() < System.currentTimeMillis()) {
            long times = System.currentTimeMillis() - parseDate.getTime();
            long haslost = times - 60 * 60 * 1000;
            if (haslost > 0) {
                result = true;
            }
        }
        return result;
    }

    //自定义定相差的小时
    public static Boolean duringAutoHour(Integer datetime, Integer hours) {
        Boolean result = false;
        //只判断过去的时间 是否 在72小时之后
        long times = System.currentTimeMillis() / 1000 - datetime;
        logger.debug("当前时间:" + format(System.currentTimeMillis() / 1000 + "", "yyyy-MM-dd HH:mm:ss"));
        logger.debug("发货时间" + format(datetime + "", "yyyy-MM-dd HH:mm:ss"));
        int autoReceTime = hours * 60 * 60;
//        int autoReceTime = 5 * 60;
        long haslost = times - autoReceTime;
        if (haslost >= 0) {
            result = true;
        }

        return result;
    }


//	public static void main(String[] args) throws ParseException, BusinessException {
//		List<String> array = getDaysAfterArray("2016-05-01", "2016-05-06");
//		System.out.println(JSONUtil.objToStr(array));
//		Date date = new Date();
//		System.out.println(date.get);
//		Date date = new Date(1478245958* 1000l);
//		System.out.println(date);
//
//		Calendar c = Calendar.getInstance();
//		System.out.println("当前" + c.get(Calendar.HOUR_OF_DAY) + "点");
//
//		String year = getDateFormatStringFromSeconds(getCurrentTimeSecond());
//		System.out.print(year);
//
//	}


    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZ");

    static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");


    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }

    public static Date minusOneDay(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }

    public static String format(Date date) {
        return format.format(date);
    }

    public static Date parse(String date) throws ParseException {
        return format.parse(date);
    }

    // 格式化秒数时间戳
    public static String format(int timestamp, String pattern) throws ParseException {
        if (timestamp == 0) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date((long) timestamp * 1000);
        String format = dateFormat.format(date);
        return format;
    }

    // 将字符串的秒数时间戳格式化
    public static String format(String timestamp, String pattern) {
        if (StringUtils.isBlank(timestamp)) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(Long.valueOf(timestamp) * 1000);
        String format = dateFormat.format(date);
        return format;
    }

    // 格式化日期-timestamp(毫秒)
    public static String format(Long timestamp, String pattern) throws ParseException {
        if (null == timestamp) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date(timestamp));
    }

    // 格式化日期
    public static String format(Date time, String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(time);
    }

    // 将字符串格式日期, 转换成秒数时间戳
    public static long getSecondByStringDate(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_1);
        Date date = sdf.parse(time);
        return date.getTime() / 1000;
    }

    public static int getCurrentTimeSecond() {
        long time = System.currentTimeMillis() / 1000;
        return (int) time;
    }

    public static void main(String[] args) throws ParseException {
//        System.out.println(getMondayOfThisWeek("2017-3-16"));
//        System.out.println(getCurrentTimeMillSecond() - 86400000);
//        System.out.println(getCurrentTimeMillSecond() + 86400000);
//        System.out.println(format()new Date(),PATTERN_DATE_1);
//        System.out.println(format(new Date(), PATTERN_DATE_1));
//
//
//        System.out.println(getTodayEndTimesSecond());
//        System.out.println(format(DateUtils.getDateStartTimeMillSecond(DateUtils.addDays(new Date(getTodayBeginTimesSecond() * 1000), 1)),PARTTERN_1));
        Long startTime = DateUtils.getDateStartTimeMillSecond(DateUtils.addDays(new Date(), 2 - 1));
        Long endTime = DateUtils.getDateEndTimeMillSecond(DateUtils.addDays(new Date(startTime), 1 - 1));
        System.out.println(startTime);
        System.out.println(endTime);
    }

    /**
     * 获取当前时间-精确到毫秒
     *
     * @return
     */
    public static Long getCurrentTimeMillSecond() {
        return System.currentTimeMillis();
    }

    // 获取起止日期的区间, 数组索引0为起始日期, 起止日期格式:yyyy-MM-dd
    public static long[] getDateRegion(Date begin, Date stop) throws ParseException {
        String start = format2.format(begin);
        String end = format2.format(stop);
        long[] dateRegion = new long[2];
        Date dateStart = format2.parse(start);
        dateRegion[0] = dateStart.getTime() / 1000;
        Date dateEnd = format2.parse(end);
        dateRegion[1] = dateEnd.getTime() / 1000 - 1;
        return dateRegion;
    }

    // 获取当前本周周一时间
    public static String getMondayOfThisWeek() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);
        String day = format2.format(calendar.getTime());
        return day;
    }

    // 获取当前本周周一时间
    public static String getMondayOfThisWeek(String today) throws ParseException {
        Calendar calendar = Calendar.getInstance();
//		DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        DateFormat dateInstance = new SimpleDateFormat(PATTERN_DATE_1);
        Date parse = dateInstance.parse(today);
        calendar.setTime(parse);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);
        String day = format2.format(calendar.getTime());
        return day;
    }

    // 获取本周周日
    public static String getSundayOfThisWeek() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        calendar.add(Calendar.DATE, -day_of_week + 7);
        String day = format2.format(calendar.getTime());
        return day;
    }

    // 获得本月最后一天
    public static String getLastDayOfThisMonth() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day = format2.format(calendar.getTime());
        return day;
    }

    //获取当前月第一天：
    public static String getFirstDayOfThisMonth() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String day = format2.format(calendar.getTime());
        return day;
    }

    // 拿到今天 00:00:00 的时间times long值
    public static long getTodayBeginTimesSecond() {
        long time = DateUtils.parseToDate(DateUtils.formatByDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime() / 1000;
        return time;
    }

    // 拿到今天结束的时间second值
    public static long getTodayEndTimesSecond() {
        long time = addDays(DateUtils.parseToDate(DateUtils.formatByDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"), 1).getTime();
        return (time / 1000) - 1;
    }

    // 拿到今天 00:00:00 的时间times long值
    public static long getTodayBeginTimesMillSecond() {
        long time = DateUtils.parseToDate(DateUtils.formatByDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime();
        return time;
    }

    // 拿到今天结束的时间second值
    public static long getTodayEndTimesMillSecond() {
        long time = addDays(DateUtils.parseToDate(DateUtils.formatByDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"), 1).getTime();
        return time - 1;
    }

    // 获取指定日期的起始日期秒值
    public static long getDateStartTimeSecond(Date date) {
        long time = DateUtils.parseToDate(DateUtils.formatByDate(date, "yyyy-MM-dd"), "yyyy-MM-dd").getTime();
        return time / 1000;
    }

    // 获取制定日期的结束秒值
    public static long getDateEndTimeSecond(Date date) {
        long time = addDays(DateUtils.parseToDate(DateUtils.formatByDate(date, "yyyy-MM-dd"), "yyyy-MM-dd"), 1).getTime();
        return (time / 1000) - 1;
    }


    // 获取指定日期的起始日期秒值
    public static long getDateStartTimeMillSecond(Date date) {
        long time = DateUtils.parseToDate(DateUtils.formatByDate(date, "yyyy-MM-dd"), "yyyy-MM-dd").getTime();
        return time;
    }

    // 获取制定日期的结束秒值
    public static long getDateEndTimeMillSecond(Date date) {
        long time = addDays(DateUtils.parseToDate(DateUtils.formatByDate(date, "yyyy-MM-dd"), "yyyy-MM-dd"), 1).getTime();
        return time - 1;
    }

    // 获取指定月份的起始时间戳
    public static long getStartSecondOfTargetMonth(String yearStr, String monthStr) {
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getDateStartTimeSecond(cal.getTime());
    }

    // 获取指定月份的终止时间戳
    public static long getEndSecondOfTargetMonth(String yearStr, String monthStr) {
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, -1);
        return getDateEndTimeSecond(cal.getTime());
    }


    //根据秒 转换为 yyyy-MM-dd HH:mm:ss 格式的字符串


    public static String getDateFormatStringFromSeconds(Integer seconds) {
        try {
            Long times = seconds * 1000l;
            Date date = new Date(times);
            return format(date, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMonthAndDayFromSeconds(Integer seconds) {
        Long times = seconds * 1000l;
        Date date = new Date(times);
        try {
            return format(date, "MM月dd日");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHHmmFromSeconds(Integer seconds) {
        Long times = seconds * 1000l;
        Date date = new Date(times);
        try {
            return format(date, "HH:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 如果日期0,则返回前台为空字符串
     **/
    public static String fromTimestampToString(Integer timestamp) {
        if (timestamp == null) {
            return "";
        }
        return fromTimestampToString(timestamp, PARTTERN_1);
    }

    /**
     * 如果日期0,则返回前台为空字符串
     **/
    public static String fromTimestampToString(Integer timestamp, String fmt) {
        if (null == timestamp) {
            return "";
        }
        if (Integer.valueOf(0).equals(timestamp)) {
            return "";
        }
        Date date = new Date(timestamp * 1000l);
        //1970年,直接转换为空字符串
        if (date.before(originDate)) {
            return "";
        }
        return formatDate(date, fmt);
    }


    public static Integer getSecondsByDateString(String date) {
        Long time = DateUtils.parseToDate(date, "yyyy-MM-dd").getTime() / 1000;
        return time.intValue();
    }

    public static String formatDateToSecond(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String formatDateToSecond2(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    }

    public static String formatDateToMinute(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static String formatDateToDay(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatDateToDay2(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static String formatByJsDate(Date date) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
    }

    public static String formatDateToHHmm(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }


    public static Date parseDateToSecond(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
    }

    public static Date parseDateToDay(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    public static Date parseDateToMinute(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
    }

    public static Date parseByJsDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(date);
    }

    public static String getExpireTimeDateStr() {
        long times = System.currentTimeMillis() + 30 * 60 * 1000;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(times));
    }

    public static Timestamp getExpireTimeDate() {
        long times = System.currentTimeMillis() + 30 * 60 * 1000;
        return new Timestamp(times);
    }

    public static boolean isToday(Integer timestamp) throws ParseException {
        boolean flag = false;
        String now = format(timestamp, PATTERN_DATE_1);
        String today = format(new Date(), PATTERN_DATE_1);
        if (now.equalsIgnoreCase(today)) {
            flag = true;
        }
        return flag;
    }

//	public static void main(String[] args) {
//		try {
//			System.out.println(isToday(getCurrentTimeSecond()));
//			String daysBefore = getDaysBefore(new Date(), 1);
//			Long secondByStringDate = getSecondByStringDate(daysBefore);
//			System.out.println(isToday(secondByStringDate.intValue()));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//	}

    /***
     * 获取今天是本周的周几
     */
    public static String getTodayOfWeek() {
        String todayOfWeek = "";
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case Calendar.MONDAY:
                System.out.println("1");
                todayOfWeek = "1";
                break;
            case Calendar.TUESDAY:
                System.out.println("2");
                todayOfWeek = "2";
                break;
            case Calendar.WEDNESDAY:
                System.out.println("3");
                todayOfWeek = "3";
                break;
            case Calendar.THURSDAY:
                System.out.println("4");
                todayOfWeek = "4";
                break;
            case Calendar.FRIDAY:
                System.out.println("5");
                todayOfWeek = "5";
                break;
            case Calendar.SATURDAY:
                System.out.println("6");
                todayOfWeek = "6";
                break;
            case Calendar.SUNDAY:
                System.out.println("7");
                todayOfWeek = "7";
                break;

        }
        return todayOfWeek;
    }

    public static String getCurrentTimeFlow() {
        String format = "";
        try {
            format = format(new Date(), PARTTERN_4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 获取某天是周几 0:周日
     *
     * @param data
     * @return
     */
    public static int getDayOFWeek(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return i - 1;
    }

    /**
     * 获取延后的几天
     *
     * @param dateStr
     * @param days
     * @return
     * @throws ParseException
     */
    public static String getLayDay(String dateStr, int days) throws ParseException {
        Calendar c1 = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date daystart = df.parse(dateStr);    //start_date是类似"2013-02-02"的字符串
        c1.setTime(daystart);
        int day = c1.get(Calendar.DATE);
        c1.set(Calendar.DATE, day + 1);

        String returnDay = df.format(c1.getTime());
        return returnDay;
    }

    /**
     * 计算用户的年龄 粗略计算
     */
    public static String getAge(Long birthDay){
        Date date = new Date(birthDay);
        //获取当前系统时间
        Calendar cal = Calendar.getInstance();
        //如果出生日期大于当前时间，则抛出异常
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        //取出系统当前时间的年、月、日部分
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        //将日期设置为出生日期
        cal.setTime(date);
        //取出出生日期的年、月、日部分
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        //当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth;
        int month = monthNow - monthBirth;
        if (month == 0) {
            if (dayOfMonthNow < dayOfMonthBirth) {
                month--;
            }
        }
        //当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (month < 0) {
            //如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
            age--;
            month += 12;
        }

        return age + "周岁" + month + "个月";
    }
}
