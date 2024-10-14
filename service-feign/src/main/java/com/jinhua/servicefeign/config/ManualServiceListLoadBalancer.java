package com.jinhua.servicefeign.config;

import com.jinhua.feigncommon.util.NetStateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
public class ManualServiceListLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private ConfigUrlAndTimeoutConfig configUrlAndTimeoutConfig;

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    @Autowired
    public ManualServiceListLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier instanceListSupplier = serviceInstanceListSupplierProvider.getIfAvailable();
        return instanceListSupplier.get().next().map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances) {
        String url = getReachableUrl();
        DefaultServiceInstance customInstance = new DefaultServiceInstance();
        try {
            customInstance.setUri(new URI(url));
        } catch (URISyntaxException ignored) {
        }
        return new DefaultResponse(customInstance);
    }

    private String getReachableUrl() {
        List<String> urlList = Optional.ofNullable(
                configUrlAndTimeoutConfig.getUrlList()
        ).orElse(Collections.emptyList());
        for (String url : urlList) {
            String ip = resolveIp(url);
            Integer port = resolvePort(url);

            if (NetStateUtil.isReachable(ip, port, configUrlAndTimeoutConfig.getTestConnectTimeout())) {
                return url;
            }
        }
        throw new IllegalStateException(
                String.format("no url is reachable. urlList = %s", urlList)
        );
    }


    /**
     * 从给定URL中解析出IP
     *
     * @param configUrl 配置的URL
     * @return ConfigServer的IP
     */
    private String resolveIp(String configUrl) {
        int start = configUrl.indexOf("://");
        int end = configUrl.lastIndexOf(":");
        int first = configUrl.indexOf(":");

        if (start == -1 || end == -1 || first == end) {
            throw new IllegalStateException(
                    String.format("invalid url = %s", configUrl)
            );
        }
        return configUrl.substring(start + 3, end);
    }

    private Integer resolvePort(String url) {
        return Integer.parseInt(url.substring(url.lastIndexOf(":") + 1));
    }

    @Autowired
    public void setConfigUrlAndTimeoutConfig(ConfigUrlAndTimeoutConfig configUrlAndTimeoutConfig) {
        this.configUrlAndTimeoutConfig = configUrlAndTimeoutConfig;
    }
}
