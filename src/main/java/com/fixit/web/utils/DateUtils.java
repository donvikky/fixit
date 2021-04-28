package com.fixit.web.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public DateUtils(){
        //
    }

    public long getTimeInterval(Date date, TimeUnit timeUnit){
        long timeDifference = new Date().getTime() - date.getTime();
        return timeUnit.convert(timeDifference, TimeUnit.MILLISECONDS);
    }

    public String getTimeIntervalDisplayText(Date date){
        String duration = "";
        long daysInterval = getTimeInterval(date, TimeUnit.DAYS);
        long hoursInterval = getTimeInterval(date, TimeUnit.HOURS);
        long minutesInterval = getTimeInterval(date, TimeUnit.MINUTES);

        if(daysInterval >= 1){
            duration = daysInterval == 1 ? "day" : "days";
            return String.format("%d %s", daysInterval, duration);
        }
        if(hoursInterval >= 1){
            duration = hoursInterval == 1 ? "hour" : "hours";
            return String.format("%d %s", hoursInterval, duration);
        }
        if(minutesInterval >= 1){
            duration = minutesInterval == 1 ? "minute" : "minutes";
            return String.format("%d %s", minutesInterval, duration);
        }
        return String.format("%d seconds", getTimeInterval(date, TimeUnit.SECONDS));
    }

}
