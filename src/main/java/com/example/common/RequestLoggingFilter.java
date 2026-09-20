package com.example.common;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String id = OptionalHeader(req.getHeader("X-Request-Id"));
        MDC.put("requestId", id);
        res.setHeader("X-Request-Id", id);
        long start = System.currentTimeMillis();
        try {
            chain.doFilter(req, res);
        } finally {
            log.info("requestId={} method={} uri={} status={} durationMs={}", id, req.getMethod(), req.getRequestURI(), res.getStatus(), System.currentTimeMillis() - start);
            MDC.remove("requestId");
        }
    }

    private String OptionalHeader(String h) {
        return h == null || h.isBlank() ? UUID.randomUUID().toString() : h;
    }
}
