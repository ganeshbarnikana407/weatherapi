package com.weather.ratelimit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class RateLimitService {

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_TIME_MS = 60_000; // 1 minute

    private final Map<String, RequestInfo> requestMap = new ConcurrentHashMap<>();

    public synchronized boolean allowRequest(String apiKey) {
        long currentTime = System.currentTimeMillis();

        RequestInfo info = requestMap.get(apiKey);

        if (info == null || currentTime - info.startTime > WINDOW_TIME_MS) {
            // new window
            requestMap.put(apiKey, new RequestInfo(1, currentTime));
            return true;
        }

        if (info.count < MAX_REQUESTS) {
            info.count++;
            return true;
        }

        return false; // limit exceeded
    }

    static class RequestInfo {
        int count;
        long startTime;

        RequestInfo(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}
