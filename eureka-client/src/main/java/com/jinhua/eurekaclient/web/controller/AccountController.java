package com.jinhua.eurekaclient.web.controller;

import com.jinhua.feigncommon.MyHelloDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author Jinhua
 * @version 1.0
 * @date 2022/8/12 11:43
 */
@RestController
@RequestMapping("/${spring.application.name}")
public class AccountController {

    @LoadBalanced
    @Resource
    private RestTemplate restTemplate;

    @Value(value = "${server.port}")
    private Integer port;

    @PostMapping(value = "/hello")
    public String home(@RequestBody MyHelloDTO hello) {
        return "Hello, " + hello.getName() + "! I am from port: " + port;
    }
}
