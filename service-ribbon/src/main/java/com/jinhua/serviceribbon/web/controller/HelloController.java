package com.jinhua.serviceribbon.web.controller;

import com.jinhua.serviceribbon.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jinhua-Lee
 */
@RestController
@RequestMapping("/${spring.application.name}")
public class HelloController {

    private HelloService helloService;

    @GetMapping(value = "/hello")
    public String hello(@RequestParam(value = "name") String name) {
        return helloService.helloService(name);
    }

    @Autowired
    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }
}
