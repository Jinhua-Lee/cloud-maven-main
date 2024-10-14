package com.jinhua.servicefeign.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Jinhua-Lee
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "service.eureka-client")
public class ConfigUrlAndTimeoutConfig implements InitializingBean {
    private List<String> urlList;

    private Integer connectionRequestTimeout;
    private Integer connectTimeout;
    private Integer readTimeout;
    private Integer testConnectTimeout;

    @Override
    public void afterPropertiesSet() {
        if (ObjectUtils.isEmpty(urlList)) {
            throw new IllegalArgumentException("configServerUrls can not be empty.");
        }
        if (ObjectUtils.isEmpty(connectionRequestTimeout)) {
            connectionRequestTimeout = 5000;
        }
        if (ObjectUtils.isEmpty(connectTimeout)) {
            connectTimeout = 5000;
        }
        if (ObjectUtils.isEmpty(readTimeout)) {
            readTimeout = 100000;
        }
        if (ObjectUtils.isEmpty(testConnectTimeout)) {
            testConnectTimeout = 1000;
        }
    }
}