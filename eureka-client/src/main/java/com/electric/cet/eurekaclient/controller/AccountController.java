package com.electric.cet.eurekaclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "/hello")
    public String home(@RequestParam(value = "name", defaultValue = "def") String name) {
        return "Hello, " + name + "! I am from port: " + port;
    }
}
