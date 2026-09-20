package com.example.config;

import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(RepositoryLoggingAspect.class);

    @Around("execution(* com.example..*Repository+.*(..))")
    public Object log(org.aspectj.lang.ProceedingJoinPoint p) throws Throwable {
        long s = System.currentTimeMillis();
        try {
            return p.proceed();
        } finally {
            log.info("repository method={} durationMs={}", p.getSignature().toShortString(), System.currentTimeMillis() - s);
        }
    }
}
