package com.jinhua.servicefeign.config;

import com.netflix.loadbalancer.BaseLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Jinhua-Lee
 */
@Slf4j
// @Component
public class ManualServiceListLoadBalancer extends BaseLoadBalancer {

    private ConfigUrlAndTimeoutConfig configUrlAndTimeoutConfig;
}
