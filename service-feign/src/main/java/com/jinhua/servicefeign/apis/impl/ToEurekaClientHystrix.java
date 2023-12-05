package com.jinhua.servicefeign.apis.impl;

import com.jinhua.feigncommon.MyHelloDTO;
import com.jinhua.servicefeign.apis.ToEurekaClient;
import org.springframework.stereotype.Component;

/**
 * @author Jinhua
 */
@Component
public class ToEurekaClientHystrix implements ToEurekaClient {

    @Override
    public String helloFromClient(MyHelloDTO hello) {
        return hello.getName();
    }
}
