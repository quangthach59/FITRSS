package com.hcmus.fitrss.utils;

import com.hcmus.fitrss.config.AppConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The UtilsTime class helps convert between Date and string.
 */
public class UtilsTime {
    /**
     * Parse a formatted string to Date
     *
     * @param timeString Given string
     * @return Date
     * @throws Exception when string is not formatted
     */
    public static Date parse(String timeString, String format) throws Exception {
        Locale locale = Locale.getDefault();
        return new SimpleDateFormat(format, locale).parse(timeString);
    }

    /**
     * Convert a given Date to string with short format
     *
     * @param date   Given date
     * @param format Format
     * @return formatted string
     */
    public static String toFormat(Date date, String format) {
        return new SimpleDateFormat(format, Locale.getDefault())
                .format(date);
    }
}
