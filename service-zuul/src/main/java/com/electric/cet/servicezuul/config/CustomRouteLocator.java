package com.electric.cet.servicezuul.config;

import com.electric.cet.servicezuul.service.CustomUrlAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.util.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
public class CustomRouteLocator implements RouteLocator {

    private CustomUrlAvailabilityService customUrlAvailabilityService;

    private final PathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<String> getIgnoredPaths() {
        return Collections.emptyList();
    }

    @Override
    public List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        customUrlAvailabilityService.getAllReachableUrlsByServiceName().forEach((serviceName, urls) ->
                urls.forEach(url -> {
                    Route route = new Route(serviceName, "/**", url,
                            "/" + serviceName, false, null
                    );
                    routes.add(route);
                })
        );
        return routes;
    }

    @Override
    public Route getMatchingRoute(final String path) {
        log.debug("[custom-routing] Finding route for path: {}", path);
        log.debug("[custom-routing] RequestUtils.isDispatcherServletRequest()= {}",
                RequestUtils.isDispatcherServletRequest());
        log.debug("[custom-routing] RequestUtils.isZuulServletRequest()= {}",
                RequestUtils.isZuulServletRequest());

        Map<String, Set<String>> allReachableUrlsByServiceName = customUrlAvailabilityService
                .getAllReachableUrlsByServiceName();

        return allReachableUrlsByServiceName.entrySet().stream().filter((bySrvName) -> {
            // 配置中服务名构造pattern
            String pattern = "/" + bySrvName.getKey() + "/**";
            // 与实际路径匹配
            return antPathMatcher.match(pattern, path);
        }).findFirst().map(bySrvName -> {
            String srvName = bySrvName.getKey();
            // 不会存在value为empty或者null的情况，service结果返回时，非empty才设置进来
            String location = bySrvName.getValue().iterator().next();
            log.debug("[custom-routing] Found route for path {} with location {}", path, location);

            int index = path.substring(1).indexOf("/");
            String prefix = location.endsWith("/") ? "" : "/";
            // 构造实际的路由路径
            return new Route(srvName, path.substring(index + 1), location,
                    prefix, false, null
            );
        }).orElseGet(() -> {
            // 当对应服务没找到路由时，就不走这里，再通过CompositeRouteLocator去找其他路由
            log.warn("[custom-routing] No route found for path: {}, current reachable locations are: {}",
                    path, allReachableUrlsByServiceName);
            return null;
        });
    }

    @Autowired
    public void setCustomUrlAvailabilityService(CustomUrlAvailabilityService customUrlAvailabilityService) {
        this.customUrlAvailabilityService = customUrlAvailabilityService;
    }
}
