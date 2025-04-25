package com.aspacelifetech.interviewservice.handlers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @Author: Idris Ishaq
 * @Date: 09 Apr, 2023
 */


public class DateTimeHandler {

    private final static String TIME_FORMAT = "hh:mm:ss a";
    private final static String DATE_FORMAT = "yyyy-MM-dd";

    public static TimeZone getZoneId() {
        return TimeZone.getTimeZone("GMT+01:00");
    }

    public static String getDate() {
        LocalDate localDate = getLocalDate();
        return localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDate getLocalDate() {
        return LocalDateTime.now(getZoneId().toZoneId()).toLocalDate();
    }

    public static String getTime() {
        LocalTime localTime = LocalTime.now(getZoneId().toZoneId());
        return localTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public static String dateTime() {
        return String.format("%s, %s", getDate(), getTime());
    }

    public static String dateTimeStamp() {
        LocalDateTime localDateTime = LocalDateTime.now(getZoneId().toZoneId()).minusHours(1);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    public static Instant getDateInstant() {
        LocalDate localDate = DateTimeHandler.getLocalDate();
        return localDate.atStartOfDay().atZone(getZoneId().toZoneId()).toInstant();
    }

    public static LocalDate toLocalDate(String date) {
        String[] dateArr = date.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = Integer.parseInt(dateArr[1]);
        int day = Integer.parseInt(dateArr[2]);
        return LocalDate.of(year, month, day);
    }

}
