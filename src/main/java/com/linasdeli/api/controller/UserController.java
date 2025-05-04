package com.linasdeli.api.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Object securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("securityContext", securityContext); // this can be null

        return ResponseEntity.ok(response);
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> getSessionInfo(HttpServletRequest request, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        log.info("Checking session");
        String sessionId = request.getSession(false) != null ? request.getSession(false).getId() : null;
        log.info("Session ID: {}", sessionId);
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", userDetails.getUsername());
            userData.put("roles", userDetails.getAuthorities());
            userData.put("sessionId", sessionId);

            response.put("authenticated", true);
            response.put("user", userData);
            log.info("User data: {}", userData);
            return ResponseEntity.ok(response);
        } else {
            response.put("authenticated", false);
            response.put("message", "User not authenticated");
            log.info("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String id, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(id, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            HttpSession session = request.getSession(true);

            // 세션 쿠키를 클라이언트로 전달
            Cookie cookie = new Cookie("JSESSIONID", session.getId());
            cookie.setPath("/");
            cookie.setHttpOnly(true);  // 보안 강화를 위해 HttpOnly 설정
            cookie.setMaxAge(60 * 60);  // 1시간 동안 유효

            response.addCookie(cookie);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Login successful");
            responseMap.put("user", Map.of(
                    "id", userDetails.getUsername(),
                    "username", userDetails.getUsername(),
                    "sessionId", session.getId(),
                    "role", userDetails.getAuthorities().iterator().next().getAuthority()
            ));

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            log.error("Login failed for user {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}