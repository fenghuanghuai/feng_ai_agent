package com.qcdfz.fengaiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * @version v1.0.0
 * @belongsProject: feng-ai-agent
 * @belongsPackage: com.qcdfz.fengaiagent.tools
 * @author: fgh
 * @description: 时间工具类
 * @createTime: 2025-05-17 19:49
 */
@Slf4j
public class TimeTool {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间信息
     * @return 包含当前日期、时间、时间戳的Map
     */
    @Tool(description = "Get current time information")
    public Map<String, String> getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> timeInfo = new HashMap<>();

        timeInfo.put("date", now.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        timeInfo.put("time", now.format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        timeInfo.put("datetime", now.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)));
        timeInfo.put("timestamp", String.valueOf(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

        return timeInfo;
    }

    /**
     * 计算两个日期之间的天数差
     * @param startDate 开始日期 (yyyy-MM-dd)
     * @param endDate 结束日期 (yyyy-MM-dd)
     * @return 天数差
     */
    @Tool(description = "Calculate days between two dates")
    public long calculateDaysBetween(
            @ToolParam(description = "Start date (yyyy-MM-dd)") String startDate,
            @ToolParam(description = "End date (yyyy-MM-dd)") String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            return ChronoUnit.DAYS.between(start, end);
        } catch (Exception e) {
            log.error("日期计算错误: {}", e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 格式化时间戳
     * @param timestamp 时间戳（毫秒）
     * @return 格式化后的日期时间字符串
     */
    @Tool(description = "Format timestamp to datetime string")
    public String formatTimestamp(
            @ToolParam(description = "Timestamp in milliseconds") long timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(timestamp),
                    ZoneId.systemDefault()
            );
            return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
        } catch (Exception e) {
            log.error("时间戳格式化错误: {}", e.getMessage(), e);
            return "Invalid timestamp";
        }
    }

    /**
     * 获取指定日期的开始和结束时间
     * @param date 日期 (yyyy-MM-dd)
     * @return 包含开始时间和结束时间的Map
     */
    @Tool(description = "Get start and end time of a specific date")
    public Map<String, String> getDayStartEndTime(
            @ToolParam(description = "Date (yyyy-MM-dd)") String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            LocalDateTime startOfDay = localDate.atStartOfDay();
            LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

            Map<String, String> result = new HashMap<>();
            result.put("startTime", startOfDay.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)));
            result.put("endTime", endOfDay.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)));

            return result;
        } catch (Exception e) {
            log.error("日期解析错误: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid date format");
            return error;
        }
    }

    /**
     * 判断日期是否在指定范围内
     * @param date 要判断的日期 (yyyy-MM-dd)
     * @param startDate 开始日期 (yyyy-MM-dd)
     * @param endDate 结束日期 (yyyy-MM-dd)
     * @return 是否在范围内
     */
    @Tool(description = "Check if a date is within a date range")
    public boolean isDateInRange(
            @ToolParam(description = "Date to check (yyyy-MM-dd)") String date,
            @ToolParam(description = "Start date (yyyy-MM-dd)") String startDate,
            @ToolParam(description = "End date (yyyy-MM-dd)") String endDate) {
        try {
            LocalDate checkDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));

            return !checkDate.isBefore(start) && !checkDate.isAfter(end);
        } catch (Exception e) {
            log.error("日期范围判断错误: {}", e.getMessage(), e);
            return false;
        }
    }
}
