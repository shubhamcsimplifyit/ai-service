package com.aiservice.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public final class DateUtil {

    DateUtil() {

    }

    public static final String DATE_FORMATTER_UI = "dd-MM-yyyy";
    public static final String DATE_TIME_FORMATTER_UI = "dd-MM-yyyy HH:mm:ss";

    public static final String DATE_TIME_FORMATTER_DB = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMATTER_DB = "yyyy-MM-dd";
    public static final String DATE_ATTACHMENT_APPROVAL_FORMAT = "dd-mm-yyyy";
    public static final String DATE_ATTACHMENT_UPLOADED_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final String TIME_ZONE_IST = "IST";

    public static final String DATE_TIME_FORMAT_DB = "yyyy-MM-ddTHH:mm:ss.SSSZ";
    
    public static String convertDateToString(Date date, String formatter) {
        if (date == null || StringUtils.isAllBlank(formatter)) {
            return null;
        }

        // Converts the string
        // format to date object
        DateFormat df = new SimpleDateFormat(formatter);

        // Convert the date into a
        // string using format() method
        String dateToString = df.format(date);

        // Return the result
        return (dateToString);
    }

    public static Date convertStringToDate(String sDate,String format) {
        if(sDate==null)
                    return null;

        Date pDate=null;
        SimpleDateFormat returnDateFormatter = new SimpleDateFormat(format);
        try {
                    pDate = (Date)returnDateFormatter.parse(sDate);
        } catch (Exception e) {
                    System.out.println("Error while Date to String conversion : "
                                            + e.getLocalizedMessage());
        }
        return pDate;
    }




    
}