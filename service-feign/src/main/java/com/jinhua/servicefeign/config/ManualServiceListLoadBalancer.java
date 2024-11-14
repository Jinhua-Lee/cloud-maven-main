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
import java.util.List;

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
        String url = NetStateUtil.getReachableUrl(
                configUrlAndTimeoutConfig.getUrlList(),
                configUrlAndTimeoutConfig.getTestConnectTimeout()
        );
        DefaultServiceInstance customInstance = new DefaultServiceInstance();
        try {
            customInstance.setUri(new URI(url));
        } catch (URISyntaxException ignored) {
        }
        return new DefaultResponse(customInstance);
    }

    @Autowired
    public void setConfigUrlAndTimeoutConfig(ConfigUrlAndTimeoutConfig configUrlAndTimeoutConfig) {
        this.configUrlAndTimeoutConfig = configUrlAndTimeoutConfig;
    }
}
