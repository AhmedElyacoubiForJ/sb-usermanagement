package edu.yacoubi.usermanagement.utility;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtility {

    public static final String getApplicationUrl(HttpServletRequest request) {
        String requestUrl = getRequestUrl(request);
        String servletPath = getServletPath(request);

        return requestUrl.replace(servletPath, "");
    }

    // get request url from HttpServletRequest
    private static String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }
    // get servlet path from HttpServletRequest
    private static String getServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }
}
