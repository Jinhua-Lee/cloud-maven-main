package com.jinhua.servicefeign.apis;

import com.jinhua.feigncommon.MyHelloDTO;
import com.jinhua.servicefeign.apis.impl.ToEurekaClientHystrix;
import com.jinhua.servicefeign.config.ManualServiceListLoadBalancer;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Jinhua-Lee
 */
@FeignClient(value = "eureka-client", fallback = ToEurekaClientHystrix.class)
@LoadBalancerClient(value = "eureka-client", configuration = ManualServiceListLoadBalancer.class)
public interface ToEurekaClient {

    /**
     * 来自client端的注释
     *
     * @param hello 入参DTO
     * @return hello string.
     */
    @PostMapping("/eureka-client/hello")
    List<String> helloFromClient(@RequestBody MyHelloDTO hello);

}
