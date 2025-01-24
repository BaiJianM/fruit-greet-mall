package com.liyuyouguo.common.utils;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author baijianmin
 */
public class DateUtils {

    /**
     * 默认时间格式
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;

    private DateUtils() {
    }

    /**
     * 字符串转LocalDateTime
     *
     * @param timeStr 待转化的时间字符串
     * @return LocalDateTime 时间字符串
     */
    public static LocalDateTime parseTime(String timeStr) {
        return LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 字符串转LocalDateTime
     *
     * @param timeStr    待转化的时间字符串
     * @param timeFormat 时间格式
     * @return LocalDateTime 时间字符串
     */
    public static LocalDateTime parseTime(String timeStr, TimeFormat timeFormat) {
        return LocalDateTime.parse(timeStr, timeFormat.formatter);
    }

    /**
     * LocalDateTime转字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @param time 时间对象
     * @return String 时间字符串
     */
    public static String parseTime(LocalDateTime time) {
        return DEFAULT_DATETIME_FORMATTER.format(time);
    }

    /**
     * 按指定格式将LocalDateTime转成字符串
     *
     * @param time   时间对象
     * @param format 时间格式
     * @return String 指定格式的时间字符串
     */
    public static String parseTime(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * 获取当前时间字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @return String 当前时间字符串
     */
    public static String now() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 以指定格式获取当前时间字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @param timeFormat 时间格式
     * @return String 格式化的时间字符串
     */
    public static String now(TimeFormat timeFormat) {
        return timeFormat.formatter.format(LocalDateTime.now());
    }

    /**
     * 获取指定时间当天的开始时间
     *
     * @param d 目标时间（类型：Date）
     * @return Date 目标时间的开始时间
     */
    public static Date getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取指定时间当天的结束时间
     *
     * @param d 目标时间（类型：Date）
     * @return Date 目标时间的结束时间
     */
    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取指定时间当天的开始时间
     *
     * @param ld 目标时间（类型：LocalDateTime）
     * @return Date 目标时间的开始时间
     */
    public static LocalDateTime getDayStartTime(LocalDateTime ld) {
        if (null == ld) {
            ld = LocalDateTime.now();
        }
        return ld.withHour(0).withMinute(0).withSecond(0);
    }

    /**
     * 获取指定时间当天的结束时间
     *
     * @param ld 目标时间（类型：LocalDateTime）
     * @return Date 目标时间的结束时间
     */
    public static LocalDateTime getDayEndTime(LocalDateTime ld) {
        if (null == ld) {
            ld = LocalDateTime.now();
        }
        return ld.withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * 获取两个时间的各类时差
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return Between 时差对象
     */
    public static Between between(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        // 两个时间秒数差
        long secondsBetween = ChronoUnit.SECONDS.between(start, end);
        // 使用秒数差创建一个Duration对象
        Duration duration = Duration.ofSeconds(secondsBetween);
        return new Between()
                // 年数
                .setYear(betweenWithYear(start.toLocalDate(), end.toLocalDate()))
                // 月份数
                .setMonth(betweenWithMonth(start.toLocalDate(), end.toLocalDate()))
                // 天数
                .setDays(duration.toDays())
                // 小时数
                .setHours(duration.toHours())
                // 分钟数
                .setMinutes(duration.toMinutes())
                // 秒数
                .setSeconds(duration.getSeconds());
    }

    /**
     * 获取两个日期相隔年数差
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return int 年数差
     */
    public static int betweenWithYear(@NonNull LocalDate start, @NonNull LocalDate end) {
        return Period.between(start, end).getYears();
    }

    /**
     * 获取两个日期相隔月份差
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return int 月份差
     */
    public static int betweenWithMonth(@NonNull LocalDate start, @NonNull LocalDate end) {
        return Period.between(start, end).getMonths();
    }

    /**
     * 获取两个日期相隔天数差
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return int 天数差
     */
    public static int betweenWithDay(@NonNull LocalDate start, @NonNull LocalDate end) {
        return Period.between(start, end).getDays();
    }

    /**
     * Date转LocalDate（注意：转换后的日期不含时间）
     *
     * @param d 待转旧版日期
     * @return LocalDate 新版日期
     */
    public static LocalDate dateToLocalDate(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDate转Date（注意：转换后的日期不含时间）
     *
     * @param ld 待转新版日期
     * @return Date 旧版日期
     */
    public static Date localDateToDate(LocalDate ld) {
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDate（注意：转换后的日期不含时间）
     *
     * @param d      待转旧版日期
     * @param zoneId 时区
     * @return LocalDate 新版日期
     */
    public static LocalDate dateToLocalDate(Date d, ZoneId zoneId) {
        return d.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * LocalDate转Date（注意：转换后的日期不含时间）
     *
     * @param ld     待转新版日期
     * @param zoneId 时区
     * @return Date 旧版日期
     */
    public static Date localDateToDate(LocalDate ld, ZoneId zoneId) {
        return Date.from(ld.atStartOfDay(zoneId).toInstant());
    }

    /**
     * Date转LocalDateTime
     *
     * @param d 待转旧版日期
     * @return LocalDateTime 新版时间
     */
    public static LocalDateTime dateToLocalDateTime(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime转Date
     *
     * @param ld 待转新版时间
     * @return Date 旧版日期
     */
    public static Date localDateTimeToDate(LocalDateTime ld) {
        return Date.from(ld.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDateTime
     *
     * @param d      待转旧版日期
     * @param zoneId 时区
     * @return LocalDateTime 新版时间
     */
    public static LocalDateTime dateToLocalDateTime(Date d, ZoneId zoneId) {
        return d.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime转Date
     *
     * @param ld     待转新版时间
     * @param zoneId 时区
     * @return Date 旧版日期
     */
    public static Date localDateTimeToDate(LocalDateTime ld, ZoneId zoneId) {
        return Date.from(ld.atZone(zoneId).toInstant());
    }

    /**
     * 时间格式内部枚举类
     *
     * @author baijianmin
     * @date 2023-09-14 17:23
     */
    public enum TimeFormat {
        // 短时间格式 年月日
        /**
         * 时间格式：yyyy-MM-dd
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        /**
         * 时间格式：yyyy/MM/dd
         */
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        /**
         * 时间格式：yyyy\\MM\\dd
         */
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        /**
         * 时间格式：yyyyMMdd
         */
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

        // 长时间格式 年月日时分秒
        /**
         * 时间格式：yyyy-MM-dd HH:mm:ss
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),

        /**
         * 时间格式：yyyy/MM/dd HH:mm:ss
         */
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        /**
         * 时间格式：yyyy\\MM\\dd HH:mm:ss
         */
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        /**
         * 时间格式：yyyyMMdd HH:mm:ss
         */
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),
        /**
         * 时间格式：yyyyMMddHHmmss
         */
        LONG_DATE_PATTERN_NONE_OTHER("yyyyMMddHHmmss"),

        // 长时间格式 年月日时分秒 带毫秒
        LONG_DATE_PATTERN_MILLISECOND_LINE("yyyy-MM-dd HH:mm:ss.SSS"),

        LONG_DATE_PATTERN_MILLISECOND_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),

        LONG_DATE_PATTERN_MILLISECOND_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),

        LONG_DATE_PATTERN_MILLISECOND_NONE("yyyyMMdd HH:mm:ss.SSS");

        private final transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    /**
     * 两个时间的相隔时差信息
     *
     * @author baijianmin
     * @date 2023-09-14 16:51
     */
    @Data
    @Accessors(chain = true)
    public static class Between {
        /**
         * 相隔年数
         */
        private long year;
        /**
         * 相隔月份数
         */
        private long month;
        /**
         * 相隔天数
         */
        private long days;
        /**
         * 相隔小时数
         */
        private long hours;
        /**
         * 相隔分钟数
         */
        private long minutes;
        /**
         * 相隔秒数
         */
        private long seconds;

        public String toJSONString() {
            return JSON.toJSONString(this);
        }
    }
}
