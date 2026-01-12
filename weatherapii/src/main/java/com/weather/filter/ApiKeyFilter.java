package com.weather.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.weather.ratelimit.RateLimitService;
import com.weather.repository.ApiKeyRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyRepository repository;

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String clientKey = request.getHeader("X-API-KEY");

        if (clientKey == null) {
            response.setStatus(401);
            response.getWriter().write("API Key missing");
            return;
        }

        boolean validKey = repository
                .findByApiKeyAndActiveTrue(clientKey)
                .isPresent();

        if (!validKey) {
            response.setStatus(401);
            response.getWriter().write("Invalid API Key");
            return;
        }

        // ✅ RATE LIMIT CHECK (NO EXTERNAL LIB)
        if (!rateLimitService.allowRequest(clientKey)) {
            response.setStatus(429);
            response.getWriter().write("Too many requests. Try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
