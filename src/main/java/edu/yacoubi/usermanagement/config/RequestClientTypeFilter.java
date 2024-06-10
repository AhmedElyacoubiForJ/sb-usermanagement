package edu.yacoubi.usermanagement.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RequestClientTypeFilter extends OncePerRequestFilter {
    private final ClientTypeHolder clientTypeHolder;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Client Type: {}", request.getHeader("User-Agent"));
        if(request.getHeader("x-api-key") != null
                && request.getHeader("x-api-key").toString().equals("from-api")) {
            log.info("Client Type: {}", request.getHeader("x-api-key"));
            clientTypeHolder.setClientType("from-api");
        }
      /*
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("headerName: {}, headerValue: {}", headerName, headerValue);
        }*/

        filterChain.doFilter(request, response);
    }
}
