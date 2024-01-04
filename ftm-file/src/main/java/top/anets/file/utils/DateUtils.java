package top.anets.file.utils;

/**
 * @author ftm
 * @date 2023-12-21 18:01
 */

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateUtils {
    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
    public static final String DEFAULT_YEAR_FORMAT = "yyyy";
    public static final String DEFAULT_MONTH_FORMAT = "yyyy-MM";
    public static final String DEFAULT_MONTH_FORMAT_SLASH = "yyyy/MM";
    public static final String DEFAULT_MONTH_FORMAT_EN = "yyyy年MM月";
    public static final String DEFAULT_WEEK_FORMAT = "yyyy-ww";
    public static final String DEFAULT_WEEK_FORMAT_EN = "yyyy年ww周";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT_EN = "yyyy年MM月dd日";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_START_FORMAT = "yyyy-MM-dd 00:00:00";
    public static final String DEFAULT_DATE_TIME_END_FORMAT = "yyyy-MM-dd 23:59:59";
    public static final String DEFAULT_DATE_TIME_FORMAT_EN = "yyyy年MM月dd日HH时mm分ss秒";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String WEEK = "WEEK";
    public static final String DEFAULT_DATE_FORMAT_MATCHES = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
    public static final String DEFAULT_DATE_TIME_FORMAT_MATCHES = "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    public static final String DEFAULT_DATE_FORMAT_EN_MATCHES = "^\\d{4}年\\d{1,2}月\\d{1,2}日$";
    public static final String DEFAULT_DATE_TIME_FORMAT_EN_MATCHES = "^\\d{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}时\\d{1,2}分\\d{1,2}秒$";
    public static final String SLASH_DATE_FORMAT_MATCHES = "^\\d{4}/\\d{1,2}/\\d{1,2}$";
    public static final String SLASH_DATE_TIME_FORMAT_MATCHES = "^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    public static final String SLASH_DATE_FORMAT = "yyyy/MM/dd";
    public static final String SLASH_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String CRON_FORMAT = "ss mm HH dd MM ? yyyy";
    public static final long MAX_MONTH_DAY = 30L;
    public static final long MAX_3_MONTH_DAY = 90L;
    public static final long MAX_YEAR_DAY = 365L;
    private static final Map<String, String> DATE_FORMAT = new LinkedHashMap(5);

    private DateUtils() {
    }

    public static LocalDate parse(String source) {
        String sourceTrim = source.trim();
        Set<Entry<String, String>> entries = DATE_FORMAT.entrySet();
        Iterator var3 = entries.iterator();

        Entry entry;
        do {
            if (!var3.hasNext()) {
                throw BizException.wrap("解析日期失败, 请传递正确的日期格式", new Object[0]);
            }

            entry = (Entry) var3.next();
        } while (!sourceTrim.matches((String) entry.getValue()));

        return LocalDate.parse(source, DateTimeFormatter.ofPattern((String) entry.getKey()));
    }

    public static String getCron(Date date) {
        return format(date, "ss mm HH dd MM ? yyyy");
    }

    public static String getCron(LocalDateTime date) {
        return format(date, "ss mm HH dd MM ? yyyy");
    }

    public static String format(LocalDateTime date, String pattern) {
        if (date == null) {
            date = LocalDateTime.now();
        }

        if (pattern == null) {
            pattern = "yyyy-MM";
        }

        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            date = LocalDate.now();
        }

        if (pattern == null) {
            pattern = "yyyy-MM";
        }

        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(Date d, String f) {
        Date date = d;
        String format = f;
        if (d == null) {
            date = new Date();
        }

        if (f == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String formatAsDate(LocalDateTime date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String formatAsDate(LocalDate date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String formatAsDateEn(LocalDateTime date) {
        return format(date, "yyyy年MM月dd日");
    }

    public static String formatAsYearMonth(LocalDateTime date) {
        return format(date, "yyyy-MM");
    }

    public static String formatAsYearMonthEn(LocalDateTime date) {
        return format(date, "yyyy年MM月");
    }

    public static String formatAsYearWeek(LocalDateTime date) {
        return format(date, "yyyy-ww");
    }

    public static String formatAsYearWeekEn(LocalDateTime date) {
        return format(date, "yyyy年ww周");
    }

    public static String formatAsYearMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(date);
    }

    public static String formatAsYearWeek(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-ww");
        return df.format(date);
    }

    public static String formatAsTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(date);
    }

    public static String formatAsDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String formatAsDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String formatAsDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd");
        return df.format(date);
    }

    public static Date parse(String dateStr, String format) {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);

        try {
            date = dateFormat.parse(dateStr);
        } catch (Exception var5) {
            log.info("DateUtils error", var5);
        }

        return date;
    }

    public static Date getLastDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, 1);
        calendar.set(5, 0);
        return calendar.getTime();
    }

    public static Date parseAsDate(String source) {
        String sourceTrim = source.trim();
        Set entries = DATE_FORMAT.entrySet();

        try {
            Iterator var3 = entries.iterator();

            while (var3.hasNext()) {
                Entry<String, String> entry = (Entry) var3.next();
                if (sourceTrim.matches((String) entry.getValue())) {
                    return (new SimpleDateFormat((String) entry.getKey())).parse(source);
                }
            }
        } catch (ParseException var5) {
            throw BizException.wrap("解析日期失败, 请传递正确的日期格式", new Object[0]);
        }

        throw BizException.wrap("解析日期失败, 请传递正确的日期格式", new Object[0]);
    }

    public static Date parseAsDateTime(String dateTime) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return simpledateformat.parse(dateTime);
        } catch (ParseException var3) {
            return null;
        }
    }

    public static Date getDate0000(LocalDateTime value) {
        return getDate0000(value.toLocalDate());
    }

    public static Date getDate0000(Date value) {
        return getDate0000(date2LocalDate(value));
    }

    public static Date getDate0000(LocalDate value) {
        LocalDateTime todayStart = LocalDateTime.of(value, LocalTime.MIN);
        return localDateTime2Date(todayStart);
    }

    public static Date getDate2359(LocalDateTime value) {
        return getDate2359(value.toLocalDate());
    }

    public static Date getDate2359(Date value) {
        return getDate2359(date2LocalDate(value));
    }

    public static Date getDate2359(LocalDate value) {
        LocalDateTime dateEnd = LocalDateTime.of(value, LocalTime.MAX);
        return localDateTime2Date(dateEnd);
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        if (date == null) {
            return LocalDateTime.now();
        } else {
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        }
    }

    public static LocalDate date2LocalDate(Date date) {
        if (date == null) {
            return LocalDate.now();
        } else {
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDate();
        }
    }

    public static LocalTime date2LocalTime(Date date) {
        if (date == null) {
            return LocalTime.now();
        } else {
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalTime();
        }
    }

    public static LocalDateTime getDateTimeOfTimestamp(long epochMilli) {
        Instant instant = Instant.ofEpochMilli(epochMilli);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime getDateTimeOfSecond(long epochSecond) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static long until(Date endDate) {
        return LocalDateTime.now().until(date2LocalDateTime(endDate), ChronoUnit.DAYS);
    }

    public static long until(Date startDate, Date endDate) {
        return date2LocalDateTime(startDate).until(date2LocalDateTime(endDate), ChronoUnit.DAYS);
    }

    public static long until(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    public static long until(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    public static List<String> getBetweenDay(Date start, Date end) {
        return getBetweenDay(date2LocalDate(start), date2LocalDate(end));
    }

    public static List<String> getBetweenDay(String start, String end) {
        return getBetweenDay(LocalDate.parse(start), LocalDate.parse(end));
    }

    public static List<String> getBetweenDay(LocalDate startDate, LocalDate endDate) {
        return getBetweenDay(startDate, endDate, "yyyy-MM-dd");
    }

    public static List<String> getBetweenDayEn(LocalDate startDate, LocalDate endDate) {
        return getBetweenDay(startDate, endDate, "yyyy年MM月dd日");
    }

    public static List<String> getBetweenDay(LocalDate startDate, LocalDate endDate, String pattern) {
        if (pattern == null) {
            pattern = "yyyy-MM-dd";
        }

        List<String> list = new ArrayList();
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1L) {
            return list;
        } else {
            String finalPattern = pattern;
            Stream.iterate(startDate, (d) -> {
                return d.plusDays(1L);
            }).limit(distance + 1L).forEach((f) -> {
                list.add(f.format(DateTimeFormatter.ofPattern(finalPattern)));
            });
            return list;
        }
    }

    public static List<String> getBetweenWeek(Date start, Date end) {
        return getBetweenWeek(date2LocalDate(start), date2LocalDate(end));
    }

    public static List<String> getBetweenWeek(String start, String end) {
        return getBetweenWeek(LocalDate.parse(start), LocalDate.parse(end));
    }

    public static List<String> getBetweenWeek(LocalDate startDate, LocalDate endDate) {
        return getBetweenWeek(startDate, endDate, "yyyy-ww");
    }

    public static List<String> getBetweenWeek(LocalDate startDate, LocalDate endDate, String pattern) {
        List<String> list = new ArrayList();
        long distance = ChronoUnit.WEEKS.between(startDate, endDate);
        if (distance < 1L) {
            return list;
        } else {
            Stream.iterate(startDate, (d) -> {
                return d.plusWeeks(1L);
            }).limit(distance + 1L).forEach((f) -> {
                list.add(f.format(DateTimeFormatter.ofPattern(pattern)));
            });
            return list;
        }
    }

    public static List<String> getBetweenMonth(Date start, Date end) {
        return getBetweenMonth(date2LocalDate(start), date2LocalDate(end));
    }

    public static List<String> getBetweenMonth(String start, String end) {
        return getBetweenMonth(LocalDate.parse(start), LocalDate.parse(end));
    }

    public static List<String> getBetweenMonth(LocalDate startDate, LocalDate endDate) {
        return getBetweenMonth(startDate, endDate, "yyyy-MM");
    }

    public static List<String> getBetweenMonth(LocalDate startDate, LocalDate endDate, String pattern) {
        List<String> list = new ArrayList();
        long distance = ChronoUnit.MONTHS.between(startDate, endDate);
        if (distance < 1L) {
            return list;
        } else {
            Stream.iterate(startDate, (d) -> {
                return d.plusMonths(1L);
            }).limit(distance + 1L).forEach((f) -> {
                list.add(f.format(DateTimeFormatter.ofPattern(pattern)));
            });
            return list;
        }
    }

    public static String calculationEn(LocalDateTime startTime, LocalDateTime endTime, List<String> dateList) {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }

        if (endTime == null) {
            endTime = LocalDateTime.now().plusDays(30L);
        }

        return calculationEn(startTime.toLocalDate(), endTime.toLocalDate(), dateList);
    }

    public static String calculation(LocalDate startDate, LocalDate endDate, List<String> dateList) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }

        if (endDate == null) {
            endDate = LocalDate.now().plusDays(30L);
        }

        if (dateList == null) {
            dateList = new ArrayList();
        }

        long day = until(startDate, endDate);
        String dateType;
        if (day >= 0L && day <= 30L) {
            dateType = "DAY";
            ((List) dateList).addAll(getBetweenDay(startDate, endDate, "yyyy-MM-dd"));
        } else if (day > 30L && day <= 90L) {
            dateType = "WEEK";
            ((List) dateList).addAll(getBetweenWeek(startDate, endDate, "yyyy-ww"));
        } else {
            if (day <= 90L || day > 365L) {
                throw new BizException("日期参数只能介于0-365天之间");
            }

            dateType = "MONTH";
            ((List) dateList).addAll(getBetweenMonth(startDate, endDate, "yyyy-MM"));
        }

        return dateType;
    }

    public static String calculationEn(LocalDate startDate, LocalDate endDate, List<String> dateList) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }

        if (endDate == null) {
            endDate = LocalDate.now().plusDays(30L);
        }

        if (dateList == null) {
            dateList = new ArrayList();
        }

        long day = until(startDate, endDate);
        String dateType;
        if (day >= 0L && day <= 30L) {
            dateType = "DAY";
            ((List) dateList).addAll(getBetweenDay(startDate, endDate, "yyyy年MM月dd日"));
        } else if (day > 30L && day <= 90L) {
            dateType = "WEEK";
            ((List) dateList).addAll(getBetweenWeek(startDate, endDate, "yyyy年ww周"));
        } else {
            if (day <= 90L || day > 365L) {
                throw new BizException("日期参数只能介于0-365天之间");
            }

            dateType = "MONTH";
            ((List) dateList).addAll(getBetweenMonth(startDate, endDate, "yyyy年MM月"));
        }

        return dateType;
    }

    public static LocalDateTime getStartTime(String time) {
        String startTime = time;
        if (time.matches("^\\d{4}-\\d{1,2}$")) {
            startTime = time + "-01 00:00:00";
        } else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            startTime = time + " 00:00:00";
        } else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")) {
            startTime = time + ":00";
        } else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}Z$")) {
            startTime = time.replace("T", " ").substring(0, time.indexOf(46));
        }

        return LocalDateTimeUtil.beginOfDay(LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public static LocalDateTime getEndTime(String time) {
        String startTime = time;
        if (time.matches("^\\d{4}-\\d{1,2}$")) {
            Date date = parse(time, "yyyy-MM");
            date = getLastDateOfMonth(date);
            startTime = formatAsDate(date) + " 23:59:59";
        } else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            startTime = time + " 23:59:59";
        } else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")) {
            startTime = time + ":59";
        } else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}Z$")) {
            time = time.replace("T", " ").substring(0, time.indexOf(46));
            startTime = time;
        }

        return endOfDay(LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public static LocalDateTime endOfDay(LocalDateTime time) {
        return time.with(LocalTime.of(23, 59, 59, 999999000));
    }

    public static boolean between(LocalTime from, LocalTime to) {
        if (from == null) {
            throw new IllegalArgumentException("开始时间不能为空");
        } else if (to == null) {
            throw new IllegalArgumentException("结束时间不能为空");
        } else {
            LocalTime now = LocalTime.now();
            return now.isAfter(from) && now.isBefore(to);
        }
    }

    public static LocalDateTime conversionDateTime(LocalDateTime dateTime, String time) {
        if (StrUtil.isEmpty(time)) {
            return LocalDateTime.MAX;
        } else if (dateTime == null) {
            return endOfDay(LocalDateTime.now());
        } else if ("0".equals(time)) {
            return endOfDay(dateTime);
        } else {
            char unit = Character.toLowerCase(time.charAt(time.length() - 1));
            if (time.length() == 1) {
                unit = 'd';
            }

            Long lastTime = Convert.toLong(time.substring(0, time.length() - 1));
            switch (unit) {
                case 'M':
                    return dateTime.plusMonths(lastTime);
                case 'd':
                default:
                    return dateTime.plusDays(lastTime);
                case 'h':
                    return dateTime.plusHours(lastTime);
                case 'm':
                    return dateTime.plusMinutes(lastTime);
                case 's':
                    return dateTime.plusSeconds(lastTime);
                case 'w':
                    return dateTime.plusWeeks(lastTime);
                case 'y':
                    return dateTime.plusYears(lastTime);
            }
        }
    }

    static {
        DATE_FORMAT.put("yyyy-MM-dd", "^\\d{4}-\\d{1,2}-\\d{1,2}$");
        DATE_FORMAT.put("yyyy/MM/dd", "^\\d{4}/\\d{1,2}/\\d{1,2}$");
        DATE_FORMAT.put("yyyy年MM月dd日", "^\\d{4}年\\d{1,2}月\\d{1,2}日$");
    }
}