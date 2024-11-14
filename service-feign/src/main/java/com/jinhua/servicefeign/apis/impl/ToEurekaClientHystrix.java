package com.jinhua.servicefeign.apis.impl;

import com.jinhua.feigncommon.MyHelloDTO;
import com.jinhua.servicefeign.apis.ToEurekaClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Jinhua
 */
@Component
public class ToEurekaClientHystrix implements ToEurekaClient {

    @Override
    public List<String> helloFromClient(MyHelloDTO hello) {
        return Collections.singletonList(hello.getName());
    }
}
