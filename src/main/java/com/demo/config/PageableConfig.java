package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return resolver -> {
            resolver.setMaxPageSize(50);
            resolver.setFallbackPageable(PageRequest.of(0, 10));
        };
    }
}
