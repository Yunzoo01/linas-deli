package com.linasdeli.api.config;

import com.linasdeli.api.repository.UserRepository;
import com.linasdeli.api.service.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 이 어노테이션을 추가해야 합니다.
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthenticationLoggingFilter authenticationLoggingFilter; // AuthenticationLoggingFilter 주입

    public SecurityConfig(UserRepository userRepository, AuthenticationLoggingFilter authenticationLoggingFilter) {
        this.userRepository = userRepository;
        this.authenticationLoggingFilter = authenticationLoggingFilter;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CORS 설정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedOrigins(List.of("https://localhost:5173","https://linas-deli.ca","https://34.236.151.248:5173"));
        config.setAllowCredentials(true); // Credentials (쿠키, 인증 정보) 허용

        source.registerCorsConfiguration("/**", config);

        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .cors(cors -> cors.configurationSource(source)) // CORS 설정 적용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/session", "/api/auth/login").permitAll()
                        .requestMatchers("/api/staff/**").hasRole("STAFF")
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin.disable()) // Form Login 비활성화
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("{\"message\": \"Logout successful\"}");
                            // 세션 무효화
                            request.getSession().invalidate();  // 세션 무효화
                            // 또는 쿠키 삭제
                            Cookie cookie = new Cookie("JSESSIONID", null);
                            cookie.setPath("/"); // 쿠키의 경로 설정
                            cookie.setMaxAge(0); // 쿠키 만료
                            response.addCookie(cookie);
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션이 필요할 때만 생성
                        .invalidSessionUrl("/api/auth/login") // 세션이 만료되었을 때 리디렉션할 URL
                        .maximumSessions(1) // 최대 세션 수 설정
                        .expiredUrl("/api/auth/login") // 세션 만료 시 리디렉션할 URL
                )
                .addFilterBefore(authenticationLoggingFilter, org.springframework.security.web.context.SecurityContextHolderFilter.class)
                .addFilterAfter(new SecurityContextPersistenceFilter(), SecurityContextHolderFilter.class); // 필터 추가

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


}

