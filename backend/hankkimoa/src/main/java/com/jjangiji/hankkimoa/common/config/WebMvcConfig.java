package com.jjangiji.hankkimoa.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                .allowedOrigins("http://localhost:3000", "https://hankkimoa.click", "https://www.hankkimoa.click")
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.LOCATION)
                .allowCredentials(true);
    }
}
