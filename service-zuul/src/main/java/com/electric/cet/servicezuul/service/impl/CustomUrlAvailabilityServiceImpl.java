package com.electric.cet.servicezuul.service.impl;

import com.electric.cet.servicezuul.config.CustomRoutingServiceConfig;
import com.electric.cet.servicezuul.service.CustomUrlAvailabilityService;
import com.jinhua.feigncommon.util.NetStateUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
public class CustomUrlAvailabilityServiceImpl implements CustomUrlAvailabilityService {

    private final CustomRoutingServiceConfig customRoutingServiceConfig;

    /**
     * 每个服务的每个Url可用性的Map
     */
    @Getter
    private final Map<String, Map<String, Boolean>> serviceUrlStateMap = new HashMap<>();

    @Autowired
    public CustomUrlAvailabilityServiceImpl(CustomRoutingServiceConfig customRoutingServiceConfig) {
        this.customRoutingServiceConfig = customRoutingServiceConfig;
    }

    @Scheduled(fixedDelay = 10 * 1000)
    public void checkCustomUrlAvailability() {
        customRoutingServiceConfig.getServices().forEach((serviceName, urlList) ->
                serviceUrlStateMap.compute(serviceName, (srv, urlState) -> {
                    if (urlState == null) {
                        // 初始化为LinkedHashMap，主备场景下尽量打到主上面.
                        urlState = new LinkedHashMap<>(urlList.size());
                    }
                    Map<String, Boolean> finalUrlState = urlState;
                    urlList.forEach(url -> {
                        String resolveIp = NetStateUtil.resolveIp(url);
                        Integer resolvePort = NetStateUtil.resolvePort(url);
                        finalUrlState.put(url,
                                NetStateUtil.isReachable(resolveIp, resolvePort,
                                        customRoutingServiceConfig.getTestConnectTimeout())
                        );
                    });
                    return urlState;
                })
        );
        log.debug("[custom-routing] current service url state: {}", serviceUrlStateMap);
    }

    @Override
    public String getReachableUrl4Service(String serviceName) {
        return this.serviceUrlStateMap.get(serviceName).entrySet()
                .stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).findFirst().orElseThrow(() ->
                        new IllegalStateException(
                                String.format("no url is reachable. urlList = %s",
                                        this.serviceUrlStateMap.get(serviceName)
                                )
                        )
                );
    }

    @Override
    public Map<String, Set<String>> getAllReachableUrlsByServiceName() {
        Map<String, Set<String>> allReachableBySrvName = new LinkedHashMap<>();
        this.serviceUrlStateMap.forEach((srvName, url2State) -> {
            Set<String> allReachable4Srv = url2State.entrySet().stream()
                    // 可用
                    .filter(Map.Entry::getValue)
                    // URL
                    .map(Map.Entry::getKey)
                    // 与配置的路由顺序一致，LinkedHashSet
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (!ObjectUtils.isEmpty(allReachable4Srv)) {
                allReachableBySrvName.put(srvName, allReachable4Srv);
            }
        });
        log.debug("[custom-routing] current reachable services are: {}", allReachableBySrvName);
        return allReachableBySrvName;
    }
}
