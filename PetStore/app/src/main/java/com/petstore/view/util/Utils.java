package com.petstore.view.util;

import android.annotation.TargetApi;
import android.os.Build;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    /**
     * validate working hours
     * @param workingHours
     * @return
     */
    public static boolean validateOfficeHours(String workingHours) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        String[] hours = workingHours.split("\\s+");
        System.out.println(hours[1]);
        System.out.println(hours[3]);
        int sh = Integer.parseInt(hours[1].split(":")[0]);
        int eh = Integer.parseInt(hours[3].split(":")[0]);
        if(hr >eh || hr <sh) return false;
        return true;
    }
}
