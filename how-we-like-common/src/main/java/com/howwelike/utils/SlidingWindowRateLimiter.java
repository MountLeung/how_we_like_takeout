package com.howwelike.utils;

import com.howwelike.context.BaseContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class SlidingWindowRateLimiter {
    // 每个用户的滑动窗口计数器
    private final ConcurrentMap<String, SlidingWindow> userWindows =
                        new ConcurrentHashMap<>();
    // 时间窗口大小（秒）
    private final int windowSize;
    // 时间窗口内允许的最大请求次数
    private final int maxRequests;
    // 时间片段数量
    private final int bucketCount;

    public SlidingWindowRateLimiter(int windowSize, int maxRequests, int bucketCount) {
        this.windowSize = windowSize;
        this.maxRequests = maxRequests;
        this.bucketCount = bucketCount;
    }

    public boolean allowRequest(String userId) {
        // 获取或创建用户的滑动窗口
        SlidingWindow window = userWindows.computeIfAbsent(userId, k -> new SlidingWindow(windowSize, bucketCount));
        return window.allowRequest(maxRequests);
    }

    private static class SlidingWindow {
        // 每个时间片段的请求计数
        private final int[] buckets;
        // 每个时间片段的大小（毫秒）
        private final int bucketSize;
        // 当前时间片段的索引
        private int currentBucket;
        // 存储传入的 windowSize
        private final int windowSize;

        public SlidingWindow(int windowSize, int bucketCount) {
            this.windowSize = windowSize;
            this.buckets = new int[bucketCount];
            this.bucketSize = windowSize * 1000 / bucketCount;
            this.currentBucket = 0;
        }

        public synchronized boolean allowRequest(int maxRequests) {
            long now = System.currentTimeMillis();
            // 计算当前时间所在的时间片段
            int newBucket = (int) ((now % (windowSize * 1000)) / bucketSize);
            // 如果时间已经超过一个时间片段，更新窗口
            if (newBucket != currentBucket) {
                // 重置新时间片段之后的所有片段计数
                if (newBucket > currentBucket) {
                    for (int i = currentBucket + 1; i <= newBucket; i++) {
                        buckets[i] = 0;
                    }
                } else {
                    // 时间片段跨越了边界, 进入新的周期
                    for (int i = currentBucket + 1; i < buckets.length; i++) {
                        buckets[i] = 0;
                    }
                    for (int i = 0; i <= newBucket; i++) {
                        buckets[i] = 0;
                    }
                }
                currentBucket = newBucket;
            }
            // 计算当前滑动窗口内的请求总数
            int totalRequests = 0;
            for (int count : buckets) {
                totalRequests += count;
            }
            if (totalRequests >= maxRequests) {
                return false;
            }
            // 允许请求，增加当前时间片段的请求计数
            buckets[currentBucket]++;
            log.info("User id = "+ BaseContext.getCurrentId() + ", request count = " + buckets[currentBucket]);
            return true;
        }
    }

    public static void main(String[] args) {
        // 创建一个滑动窗口限流器，限制用户在 60 秒内的请求次数不超过 10 次，
        // 将 60 秒划分为 6 个时间片段
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(60, 10, 6);
        String userId = "user123";
        for (int i = 0; i < 15; i++) {
            if (limiter.allowRequest(userId)) {
                System.out.println("Request " + i + " allowed");
            } else {
                System.out.println("Request " + i + " denied");
            }
            try {
                // 模拟请求间隔
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
