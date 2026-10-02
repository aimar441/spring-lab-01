package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
@Order(4)
public class RateLimitAspect {

    private final Map<String, Counter> counters =
            new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object rateLimit(
            ProceedingJoinPoint pjp,
            RateLimit rateLimit) throws Throwable {

        String key = pjp.getSignature().toShortString();

        Counter counter = counters.computeIfAbsent(
                key,
                k -> new Counter()
        );

        synchronized (counter) {
            long now = System.currentTimeMillis();

            if (now - counter.startTime >= 60_000) {
                counter.startTime = now;
                counter.count.set(0);
            }

            int current = counter.count.incrementAndGet();

            if (current > rateLimit.perMinute()) {
                throw new IllegalStateException(
                        "Rate limit exceeded: maximum "
                                + rateLimit.perMinute()
                                + " calls per minute"
                );
            }
        }

        return pjp.proceed();
    }

    private static class Counter {

        private long startTime = System.currentTimeMillis();

        private final AtomicInteger count =
                new AtomicInteger(0);
    }
}
