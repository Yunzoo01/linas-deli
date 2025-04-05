package com.linasdeli.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Request: " + request.getRequestURI() + ", Authentication: " + authentication);

        filterChain.doFilter(request, response);

        Authentication authenticationAfter = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Response: " + response.getStatus() + ", Authentication after: " + authenticationAfter);
    }
}
