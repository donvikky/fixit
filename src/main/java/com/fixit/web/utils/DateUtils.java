package com.fixit.web.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public DateUtils(){

    }

    public long getPostIntervalInDays(Date date, TimeUnit timeUnit){
        long timeDifference = new Date().getTime() - date.getTime();
        return timeUnit.convert(timeDifference, TimeUnit.MILLISECONDS);
    }
}
