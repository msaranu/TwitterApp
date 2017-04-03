package com.codepath.apps.twitterEnhanced.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Saranu on 3/26/17.
 */

//https://gist.github.com/nesquena/f786232f5ef72f6e10a7
public class DateUtil {

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";


        try {
            if (rawJsonDate == null) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                long dateMillis = cal.getTimeInMillis();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

            } else {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            }
           /* if (relativeDate != null) {

                String[] splitString = relativeDate.split("\\s+");
                if (splitString[1] != null && splitString[1].toUpperCase().equals("SECONDS")) {
                    relativeDate = splitString[0] + 's';
                } else if (splitString[1] != null && splitString[1].toUpperCase().equals("MINUTES")) {
                    relativeDate = splitString[0] + 'm';
                } else if (splitString[1] != null && splitString[1].toUpperCase().equals("HOURS")) {
                    relativeDate = splitString[0] + 'h';
                }
                if (splitString[1] != null && splitString[1].toUpperCase().equals("DAYS")) {
                    relativeDate = splitString[0] + 'd';
                }
            }*/
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return relativeDate;
    }
}
