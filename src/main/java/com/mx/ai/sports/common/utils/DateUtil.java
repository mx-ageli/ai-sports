package com.mx.ai.sports.common.utils;

import com.mx.ai.sports.common.entity.AiSportsConstant;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * @author Mengjiaxin
 * @date 2019-08-20 16:25
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static final String TIME_PATTERN = "HH:mm";

    /**
     * 根据星期和小时分钟，生成cron表达式
     * @param weeks
     * @param localTime
     * @return
     */
    public static String getWeekCron(String[] weeks, LocalTime localTime) {
        return "0" + AiSportsConstant.SPACE +
                localTime.getMinute() + AiSportsConstant.SPACE +
                localTime.getHour() + AiSportsConstant.SPACE +
                "?" + AiSportsConstant.SPACE +
                "*" + AiSportsConstant.SPACE +
                StringUtils.join(weeks, ",") + AiSportsConstant.SPACE;
    }

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String formatCstTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return DateUtil.getDateFormat(usDate, format);
    }

    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取今天是星期几
     * @return
     */
    public static int getWeek(){
        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;
        // week为8时默认切换到1
        if (week == 8) {
            week = 1;
        }
        return week;
    }
}
