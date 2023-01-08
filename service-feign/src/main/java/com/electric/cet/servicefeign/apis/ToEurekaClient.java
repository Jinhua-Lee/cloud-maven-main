package com.electric.cet.servicefeign.apis;

import com.electric.cet.servicefeign.apis.impl.ToEurekaClientHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "eureka-client", fallback = ToEurekaClientHystrix.class)
public interface ToEurekaClient {

    @GetMapping("/eureka-client/hello")
    public String helloFromEC(@RequestParam("name") String name);

}
