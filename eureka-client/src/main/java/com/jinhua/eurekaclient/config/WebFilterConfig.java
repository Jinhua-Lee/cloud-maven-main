package com.jinhua.eurekaclient.config;

import com.jinhua.eurekaclient.web.filter.RemoteMsgFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Collections;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Configuration
public class WebFilterConfig {

    @Value("${routing.filter-pattern:/*}")
    private String routingFilterPattern;

    @Bean
    public FilterRegistrationBean<Filter> remoteMsgFilter() {
        FilterRegistrationBean<Filter> remoteMsgFilterReg = new FilterRegistrationBean<>();
        remoteMsgFilterReg.setFilter(new RemoteMsgFilter());
        remoteMsgFilterReg.setOrder(0);
        remoteMsgFilterReg.setUrlPatterns(Collections.singletonList(routingFilterPattern));
        return remoteMsgFilterReg;
    }
}
