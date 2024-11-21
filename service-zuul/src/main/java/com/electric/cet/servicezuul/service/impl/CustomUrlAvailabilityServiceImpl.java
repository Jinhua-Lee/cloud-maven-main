package com.electric.cet.servicezuul.service.impl;

import com.electric.cet.servicezuul.config.CustomRoutingServiceConfig;
import com.electric.cet.servicezuul.service.CustomUrlAvailabilityService;
import com.jinhua.feigncommon.util.NetStateUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jinhua-Lee
 */
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
}
