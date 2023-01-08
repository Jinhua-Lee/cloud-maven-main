package com.electric.cet.serviceribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String helloService(String name) {
        String result = restTemplate.getForObject("http://EUREKA-CLIENT/eureka-client/hello?name=" + name, String.class);
        System.out.println(result);
        return result;
    }

    public String hiError(String name) {
        return "Hi, " + name + ", Error";
    }

}
