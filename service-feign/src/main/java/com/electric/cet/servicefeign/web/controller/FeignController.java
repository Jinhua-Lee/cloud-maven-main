package com.electric.cet.servicefeign.web.controller;

import com.electric.cet.servicefeign.apis.ToEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Jinhua
 */
@RestController
@RequestMapping("${spring.application.name}")
public class FeignController {

    @Resource
    private ToEurekaClient toEurekaClient;

    @GetMapping("/hello")
    public String hello (@RequestParam("name") String name) {
        return toEurekaClient.helloFromClient(name);
    }

}
