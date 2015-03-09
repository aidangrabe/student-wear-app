package com.aidangrabe.common.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by aidan on 09/03/15.
 * Simple utility class for dealing with time
 */
public class TimeUtil {

    /**
     * Get the difference between two Dates as a human readable String
     * eg. "22 hours ago", "10 minutes ago" or "just now"
     * @param startDate the start Date
     * @param endDate the end Date
     * @return the human readable difference between the two Dates
     */
    public static String getDeltaTime(Date startDate, Date endDate) {

        long duration  = startDate.getTime() - endDate.getTime();

        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

        if (diffInDays > 0) {
            return String.format("%d days ago", diffInDays);
        } else if (diffInHours > 0) {
            return String.format("%d hours ago", diffInHours);
        } else if (diffInMinutes > 0) {
            return String.format("%d minutes ago", diffInMinutes);
        } else {
            return "just now";
        }

    }

}
