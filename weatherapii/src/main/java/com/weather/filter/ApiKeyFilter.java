package com.weather.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.weather.repository.ApiKeyRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyRepository repository;

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

        boolean valid = repository
                .findByApiKeyAndActiveTrue(clientKey)
                .isPresent();

        if (valid) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(401);
            response.getWriter().write("Invalid or inactive API Key");
        }
    }
}


