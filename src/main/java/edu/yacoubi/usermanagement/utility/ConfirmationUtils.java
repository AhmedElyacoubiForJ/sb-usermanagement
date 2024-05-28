package edu.yacoubi.usermanagement.utility;

import java.util.Calendar;
import java.util.Date;

public class ConfirmationUtils {
    private static int EXPIRATAION_TIME = 600; // 10 minutes

    public static Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATAION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
