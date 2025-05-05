package com.linasdeli.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 예: localhost:8080/uploads/cheese.png → 실제 uploads/cheese.png 파일 제공
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }


}