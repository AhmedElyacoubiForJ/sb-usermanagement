package edu.yacoubi.usermanagement.utility;

import java.util.Calendar;
import java.util.Date;

public class TokenExpirationTime {
    private static int EXPIRATAION_TIME = 10;

    public static Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATAION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
