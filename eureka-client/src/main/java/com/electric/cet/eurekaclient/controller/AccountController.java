package com.electric.cet.eurekaclient.controller;

import com.electric.cet.eurekaclient.domain.Account;
import com.electric.cet.eurekaclient.domain.AccountInfo;
import com.electric.cet.eurekaclient.domain.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

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


    @GetMapping(value = "/account1", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<List<Account>> getAccounts() {
        String url = "http://localhost:8080/account-service/api/v1/template";
        ResponseEntity<Result> forEntity = restTemplate.getForEntity(url, Result.class);
        Result<List<Account>> body = forEntity.getBody();
        return body;
    }

    @GetMapping(value = "/account2", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<List<Account>> getAccounts2() {
        String url = "http://localhost:8080/account-service/api/v1/template";

        ResponseEntity<Result<List<Account>>> exchange = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<Result<List<Account>>>() {
                });

        System.out.println(exchange.getBody().getData().get(0));

        return exchange.getBody();
    }

    /**
     * post请求；postForObject
     *
     * @param accountInfo 请求体
     * @return 请求结果
     */
    @PostMapping(value = "/accountInfo1", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result insertAccountInfo1(@RequestBody AccountInfo accountInfo) {
        String url = "http://localhost:8080/account-service/api/v1/account";
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        restTemplate.headForHeaders(url, headers);
        HttpEntity<AccountInfo> aihe = new HttpEntity<>(accountInfo, headers);
        return restTemplate.postForObject(url, aihe, Result.class);
    }

    /**
     * post请求：postForEntity
     *
     * @param accountInfo 请求体
     * @return 请求结果
     */
    @PostMapping(value = "accountInfo2", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result insertAccountInfo2(@RequestBody AccountInfo accountInfo) {
        String url = "http://localhost:8080/account-service/api/v1/account";
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<AccountInfo> httpEntity = new HttpEntity<>(accountInfo, headers);

        ResponseEntity<Result> resultResponseEntity = restTemplate.postForEntity(url, httpEntity, Result.class);

        return resultResponseEntity.getBody();
    }

    /**
     * post请求：exchange
     *
     * @param accountInfo 请求体
     * @return 请求结果
     */
    @RequestMapping(value = "/accountInfo3", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addAccountInfo(@RequestBody AccountInfo accountInfo) {
        String url = "http://localhost:8080/account-service/api/v1/account";
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<Result> exchange =
                restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(accountInfo, headers), Result.class);
        return exchange.getBody();
    }

}
