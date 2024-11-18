package com.electric.cet.servicezuul.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Jinhua-Lee
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(value = "zuul.manual-routing")
public class ManualRoutingServerConfig implements InitializingBean {
    private Map<String, List<String>> services;

    @Override
    public void afterPropertiesSet() {
        log.info("[manual-routing] services: start===============");
        services.forEach((name, urls) -> {
            log.info("[manual-routing] service: {}", name);
            log.info("[manual-routing] service urls: {}", urls);
        });
        log.info("[manual-routing] services: end===============");
    }
}
