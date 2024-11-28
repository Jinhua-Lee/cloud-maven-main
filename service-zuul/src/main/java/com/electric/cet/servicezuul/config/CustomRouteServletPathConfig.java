package com.electric.cet.servicezuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jinhua-Lee
 */
@Configuration
public class CustomRouteServletPathConfig {

    @Bean
    public String servletPath() {
        // 需要注入一个这样的Bean，保证CustomRouteLocator的构造
        return "/";
    }
}
