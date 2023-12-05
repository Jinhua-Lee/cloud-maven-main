package com.jinhua.servicefeign.apis;

import com.jinhua.feigncommon.MyHelloDTO;
import com.jinhua.servicefeign.apis.impl.ToEurekaClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jinhua-Lee
 */
@FeignClient(value = "eureka-client", fallback = ToEurekaClientHystrix.class)
public interface ToEurekaClient {

    /**
     * 来自client端的注释
     *
     * @param hello 入参DTO
     * @return hello string.
     */
    @GetMapping("/eureka-client/hello")
    String helloFromClient(@RequestBody MyHelloDTO hello);

}
