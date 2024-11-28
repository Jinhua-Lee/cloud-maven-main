package com.electric.cet.servicezuul.config;

import com.electric.cet.servicezuul.service.CustomUrlAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.util.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
public class CustomRouteLocator extends SimpleRouteLocator {

    private CustomUrlAvailabilityService customUrlAvailabilityService;

    private final PathMatcher antPathMatcher = new AntPathMatcher();

    public CustomRouteLocator(String servletPath,
                              ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    public List<Route> getRoutes() {
        List<Route> routes = super.getRoutes();
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
        if (log.isDebugEnabled()) {
            log.debug("[custom-routing] Finding route for path: " + path);
        }

        if (log.isDebugEnabled()) {
            log.debug("[custom-routing] RequestUtils.isDispatcherServletRequest()="
                    + RequestUtils.isDispatcherServletRequest());
            log.debug("[custom-routing] RequestUtils.isZuulServletRequest()="
                    + RequestUtils.isZuulServletRequest());
        }
        Map<String, Set<String>> allReachableUrlsByServiceName = customUrlAvailabilityService
                .getAllReachableUrlsByServiceName();

        return allReachableUrlsByServiceName.entrySet().stream().filter((bySrvName) -> {
            // 配置中服务名构造pattern
            String pattern = "/" + bySrvName.getKey() + "/**";
            // 与实际路径匹配
            return antPathMatcher.match(pattern, path);
        }).findFirst().map(bySrvName -> {
            String srvName = bySrvName.getKey();
            String location = bySrvName.getValue().iterator().next();
            if (location == null) {
                log.warn("[custom-routing] No route found for path {} with matched serviceNam {}," +
                                " current reachable locations are: {}",
                        path, srvName, allReachableUrlsByServiceName
                );
                return null;
            }
            int index = path.substring(1).indexOf("/");
            String prefix = location.endsWith("/") ? "" : "/";
            // 构造实际的路由路径
            return new Route(srvName, path.substring(index + 1), location,
                    prefix, false, null
            );
        }).orElseGet(() -> {
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
