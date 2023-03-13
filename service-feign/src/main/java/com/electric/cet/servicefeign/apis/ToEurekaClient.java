package com.electric.cet.servicefeign.apis;

import com.electric.cet.servicefeign.apis.impl.ToEurekaClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jinhua-Lee
 */
@FeignClient(value = "eureka-client", fallback = ToEurekaClientHystrix.class)
public interface ToEurekaClient {

    /**
     * 来自client端的注释
     * @param name name
     * @return hello string.
     */
    @GetMapping("/eureka-client/hello")
    String helloFromClient(@RequestParam("name") String name);

}
