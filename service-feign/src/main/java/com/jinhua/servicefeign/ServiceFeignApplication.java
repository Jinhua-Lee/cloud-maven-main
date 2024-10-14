package com.jinhua.servicefeign;

import com.jinhua.servicefeign.config.ManualServiceListLoadBalancer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Jinhua-Lee
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@LoadBalancerClient(value = "eureka-client", configuration = ManualServiceListLoadBalancer.class)
public class ServiceFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFeignApplication.class, args);
    }

}
