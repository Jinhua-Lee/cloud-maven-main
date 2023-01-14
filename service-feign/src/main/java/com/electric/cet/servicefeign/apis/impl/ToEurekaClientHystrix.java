package com.electric.cet.servicefeign.apis.impl;

import com.electric.cet.servicefeign.apis.ToEurekaClient;
import org.springframework.stereotype.Component;

/**
 * @author Jinhua
 */
@Component
public class ToEurekaClientHystrix implements ToEurekaClient {
    @Override
    public String helloFromClient(String name) {
        return "sorry, " + name;
    }
}
