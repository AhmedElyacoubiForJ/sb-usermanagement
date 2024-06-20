package edu.yacoubi.usermanagement.utility;

import edu.yacoubi.usermanagement.security.filter.ClientTypeHolder;

public class EmailUtils {
    public static String getEmailMessage(String name, String host, String token, ClientTypeHolder clientTypeHolder) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account.\n\n" +
                getVerificationUrl(host, token, clientTypeHolder) + "\n\nThe Support Team.";
    }

    public static String getVerificationUrl(String host, String token, ClientTypeHolder clientTypeHolder) {
        if (clientTypeHolder.getClientType() != null
                && clientTypeHolder.getClientType().equals("from-api")) {
            return host + "/api/v1/user/verify/account?token=" + token;
        } else {
            return host + "/registration/verifyEmail?token=" + token;
        }
    }

    public static String getResetPasswordMessage(String name,String  host,String  token) {
        return "Hello " + name + ",\n\nYour new password has been created. Please click the link below to confirm the change.\n\n" +
                getResetPasswordUrl(host, token) + "\n\nThe Support Team.";
    }

    public static String getResetPasswordUrl(String host, String token) {
        return host + "/verify/password?token=" + token;
    }
}
