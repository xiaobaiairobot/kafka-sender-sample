package com.yunli.bigdata.example.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;


/**
 * @author : yunli
 */
public class DateUtil {
  private final static String TIME_PART_MI = "MI";

  private final static String TIME_PART_H = "H";

  private final static String TIME_PART_D = "D";

  private final static String TIME_PART_W = "W";

  private final static String TIME_PART_M = "M";

  private final static String TIME_PART_Y = "Y";

  public static final String COMPACT_SHORT = "yyyyMMdd";

  public static final String SHORT = "yyyy-MM-dd";

  public static final String COMPACT_STANDARD = "yyyyMMddHHmmss";

  public static final String STANDARD = "yyyy-MM-dd HH:mm:ss";

  public static final String STANDARD_HOUR_PRECISION = "yyyy-MM-dd HH:00:00";

  public static final String COMPACT_FULL = "yyyyMMddHHmmssSSS";

  public static final String FULL = "yyyy-MM-dd HH:mm:ss.SSS";

  public enum TimeType {
    /** 分 */
    MIN,
    /** 小时 */
    HOUR,
    /** 日 */
    DAY,
    /** 周 */
    WEEK,
    /** 月 */
    MONTH,
    /** 年 */
    YEAR
  }

  /**
   * 获取零点时间
   *
   * @param date
   * @return
   */
  public static Date getStartOfDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    Date start = calendar.getTime();
    return start;
  }


  /**
   * 获取上某周的周几的开始时间
   * @param date  当前周的日期
   * @param lastWeek  上几周  1 -》 上一周， 2-》 上两周
   * @param weekNum  当前周的周几  1 - 》周日，2 -》 周一 ，3 -》周二
   * @return
   */
  public static Date getLastWeekOneDayStartDate(Date date, int lastWeek, int weekNum) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, -7 * lastWeek);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.set(Calendar.DAY_OF_WEEK, weekNum);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }


  /**
   * 获取某一天的开始时间
   * @param date  当前的计算日期
   * @param i   -1 ，表示昨天 ，1表示明天  以此类推
   * @return 返回一天的开始 时间 xxxx,xx,xx 00:00:00
   */
  public static Date getOneDayStartDate(Date date, int i) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, i);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * 获取某一天的结束时间
   * @param date  当前的计算日期
   * @param i   -1 ，表示昨天 ，1表示明天  以此类推
   * @return 返回一天的开始 时间 xxxx,xx,xx 23:59:59
   */
  public static Date getOneDayEndDate(Date date, int i) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, i);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date fromStandardString(String dateTime) throws ParseException {
    return StringUtil.hasText(dateTime) ? new SimpleDateFormat(STANDARD).parse(dateTime) : null;
  }

  public static Date fromStandardHourPrecisionString(String dateTime) throws ParseException {
    return StringUtil.hasText(dateTime) ? new SimpleDateFormat(STANDARD_HOUR_PRECISION).parse(dateTime) : null;
  }

  public static Date fromUrlSimpleString(String dateTime) throws ParseException, UnsupportedEncodingException {
    return StringUtil.hasText(dateTime) ? new SimpleDateFormat(STANDARD)
        .parse(URLDecoder.decode(dateTime, "UTF-8")) : null;
  }

  public static String toStandardString(Date date) {
    return new SimpleDateFormat(STANDARD).format(date);
  }

  public static String toStandardHourPrecisionString(Date date) {
    return new SimpleDateFormat(STANDARD_HOUR_PRECISION).format(date);
  }

  public static String toLongSimpleString(Date date) {
    return new SimpleDateFormat(COMPACT_FULL).format(date);
  }

  public static Date fromLongSimpleString(String dateTime) throws ParseException {
    return StringUtil.hasText(dateTime) ? new SimpleDateFormat(COMPACT_FULL).parse(dateTime) : null;
  }

  public static String toString(Date date, String format) {
    if (date == null || StringUtil.isEmpty(format)) {
      throw new IllegalArgumentException("format is empty");
    }
    return new SimpleDateFormat(format).format(date);
  }

  public static String instantiateParameterizedDateString(String parameterDateString, Date date) {
    if (StringUtil.isEmpty(parameterDateString)) {
      return parameterDateString;
    }
    if (StringUtil.isContainsChinese(parameterDateString)) {
      throw new RuntimeException("参数不支持中文");
    }
    boolean isSystemTime = false;
    int pos = parameterDateString.indexOf("}");
    if (pos <= 0) {
      pos = parameterDateString.indexOf("]");
      isSystemTime = true;
    }
    if (pos > 0) {
      String format = parameterDateString.substring(0, pos).replaceAll("\\{|\\[", StringUtil.EMPTY);
      Date realDate = isSystemTime ? date : DateUtils.addDays(date, -1);
      int length = parameterDateString.length();
      if (length > pos + 1) {
        // 判断是否进行了加减运算
        String expression = parameterDateString.substring(pos + 1);
        if (expression.endsWith(TIME_PART_MI)) {
          int value = Integer.parseInt(expression.replace(TIME_PART_MI, StringUtil.EMPTY));
          realDate = DateUtils.addMinutes(realDate, value);
        } else if (expression.endsWith(TIME_PART_H)) {
          int value = Integer.parseInt(expression.replace(TIME_PART_H, StringUtil.EMPTY));
          realDate = DateUtils.addHours(realDate, value);
        } else if (expression.endsWith(TIME_PART_D)) {
          int value = Integer.parseInt(expression.replace(TIME_PART_D, StringUtil.EMPTY));
          realDate = DateUtils.addDays(realDate, value);
        } else if (expression.endsWith(TIME_PART_W)) {
          int value = Integer.parseInt(expression.replace(TIME_PART_W, StringUtil.EMPTY));
          realDate = DateUtils.addWeeks(realDate, value);
        } else if (expression.endsWith(TIME_PART_M)) {
          int value = Integer.parseInt(expression.replace(TIME_PART_M, StringUtil.EMPTY));
          realDate = DateUtils.addMonths(realDate, value);
        } else if (expression.endsWith(TIME_PART_Y)) {
          int value = Integer.parseInt(expression.replace(TIME_PART_Y, StringUtil.EMPTY));
          realDate = DateUtils.addYears(realDate, value);
        } else {
          throw new IllegalArgumentException("unsupported time type:" + expression);
        }
      }
      return new SimpleDateFormat(format).format(realDate);
    } else {
      return parameterDateString;
    }
  }

  public static Map<String, String> instantiateParameterizedDateStrings(Map<String, String> parameterDateStrings,
      Date date) {
    if (CollectionUtil.isNullOrEmpty(parameterDateStrings)) {
      return parameterDateStrings;
    }
    Map<String, String> parameterizedDateStrings = new HashMap<>(parameterDateStrings.size());
    parameterDateStrings.forEach((key, value) -> {
      parameterizedDateStrings.put(key, instantiateParameterizedDateString(value, date));
    });
    return parameterizedDateStrings;
  }


  /**
   * 日期增加指定常量
   * <p>
   * 例如，在周上加1；在天上加-1
   *
   * @param date 原日期
   * @param timeType 加值的位置
   * @param value 被加值
   */
  public static Date plus(Date date, TimeType timeType, int value) {
    if (date == null || timeType == null) {
      throw new IllegalArgumentException("parameter must not be empty.");
    }
    switch (timeType) {
      case MIN:
        return toDate(toLocalDateTime(date).plusMinutes(value));
      case HOUR:
        return toDate(toLocalDateTime(date).plusHours(value));
      case DAY:
        return toDate(toLocalDateTime(date).plusDays(value));
      case WEEK:
        return toDate(toLocalDateTime(date).plusWeeks(value));
      case MONTH:
        return toDate(toLocalDateTime(date).plusMonths(value));
      case YEAR:
        return toDate(toLocalDateTime(date).plusYears(value));
      default:
        throw new IllegalArgumentException("TimeType not support.");
    }
  }

  private static Date toDate(LocalDateTime date) {
    return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
  }

  private static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}
