package com.electric.cet.eurekaserver.controller;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author Jinhua
 * @version 1.0
 * @date 2022/8/12 11:41
 */
@RestController
@RequestMapping("${spring.application.name}")
public class GreetingClientController {

    @LoadBalanced
    @Resource
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/hello")
    public String helloClient(@RequestParam("name") String name) {
        ResponseEntity<String> forEntity =
                restTemplate.getForEntity("http://localhost:5001/hello?name={1}", String.class, name);
        return forEntity.getBody();
    }
}
