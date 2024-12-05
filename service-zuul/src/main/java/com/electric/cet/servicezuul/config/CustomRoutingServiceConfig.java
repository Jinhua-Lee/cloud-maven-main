package com.electric.cet.servicezuul.config;

import com.jinhua.feigncommon.util.CommonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Jinhua-Lee
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(value = "zuul.custom-routing")
public class CustomRoutingServiceConfig implements InitializingBean {
    private Map<String, List<String>> services;
    private Integer testConnectTimeout;

    @Override
    public void afterPropertiesSet() {
        if (testConnectTimeout == null) {
            testConnectTimeout = 1000;
        }
        services.forEach((name, urls) -> {
            log.info("[custom-routing] loading config for service: {}", name);
            // 服务的自定义Url列表不能为空
            if (ObjectUtils.isEmpty(urls)) {
                log.error("[custom-routing] urls for service {} is empty, which is not allowed!", name);
                throw new IllegalStateException(
                        String.format("[custom-routing] urls for service %s is empty, which is not allowed!", name)
                );
            }
            urls.forEach(url -> {
                // 每个Url的格式正确（目前是仅支持数字类型的IP地址）
                if (!CommonUtil.isUrl(url)) {
                    log.error("[custom-routing] invalid format of url: {}", url);
                    throw new IllegalStateException(
                            String.format("[custom-routing] invalid format of url: %s", url)
                    );
                }
            });
            log.info("[custom-routing] loaded service urls: {}", urls);
        });
    }
}
