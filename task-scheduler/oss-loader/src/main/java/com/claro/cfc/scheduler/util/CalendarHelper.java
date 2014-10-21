package com.claro.cfc.scheduler.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class CalendarHelper {
    public static final Calendar toDay() {
        Calendar cal = Calendar.getInstance();
        return cal;
    }

    public static final Calendar tomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DAY_OF_YEAR, 1);
        return cal;
    }

    public static final Calendar tomorrow(int atHour) {
        Calendar cal = tomorrow();
        cal.set(Calendar.HOUR_OF_DAY, atHour);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal;
    }

    public static final Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
