package com.howwelike.interceptor;

import com.howwelike.context.BaseContext;
import com.howwelike.utils.SlidingWindowRateLimiter;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final SlidingWindowRateLimiter rateLimiter;

    private static final int SC_TOO_MANY_REQUESTS = 429;

    public RateLimitInterceptor(SlidingWindowRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = String.valueOf(BaseContext.getCurrentId());
        if (userId != null) {
            if (!rateLimiter.allowRequest(userId)) {
                response.setStatus(SC_TOO_MANY_REQUESTS);
                response.getWriter().write("Too many requests, please try again later.");
                return false;
            }
        }
        return true;
    }
}
