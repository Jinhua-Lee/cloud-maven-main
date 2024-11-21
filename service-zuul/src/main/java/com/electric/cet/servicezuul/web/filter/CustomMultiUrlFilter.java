package com.electric.cet.servicezuul.web.filter;

import com.electric.cet.servicezuul.config.CustomRoutingServiceConfig;
import com.electric.cet.servicezuul.service.CustomUrlAvailabilityService;
import com.jinhua.feigncommon.util.CommonUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
public class CustomMultiUrlFilter extends ZuulFilter {

    private CustomRoutingServiceConfig customRoutingServiceConfig;
    private CustomUrlAvailabilityService customUrlAvailabilityService;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String uri = request.getRequestURI();
        log.debug("[custom routing] request uri: {}", uri);

        String pathServiceName = resolvePathServiceName(uri);
        if (pathServiceName == null) {
            return null;
        }

        // 判断是否在配置给定的手动路由服务范围内
        if (!checkServiceCustomRoute(pathServiceName)) {
            return null;
        }

        String reachableUrl = getReachableUrl4Service(ctx, pathServiceName);
        if (reachableUrl == null) {
            return null;
        }
        log.debug("[custom routing] it's going to route to reachable url: {}", reachableUrl);
        URL routeHost = transfer2UrlObj(ctx, reachableUrl);
        if (routeHost == null) {
            return null;
        }
        ctx.setRouteHost(routeHost);
        ctx.setSendZuulResponse(true);
        return null;
    }

    private String resolvePathServiceName(String uri) {
        String pathServiceName = CommonUtil.getUriServiceName(uri);
        log.debug("[custom routing] service name: {}", pathServiceName);

        if (ObjectUtils.isEmpty(pathServiceName)) {
            log.warn("[custom routing] could not resolve service name from URI: {}", uri);
            return null;
        }
        return pathServiceName;
    }

    private boolean checkServiceCustomRoute(String pathServiceName) {
        List<String> urls = Optional.ofNullable(customRoutingServiceConfig.getServices())
                .orElse(Collections.emptyMap())
                .get(pathServiceName);
        if (ObjectUtils.isEmpty(urls)) {
            log.debug("[custom routing] service name: {} not in custom routing service list",
                    pathServiceName
            );
            return false;
        }
        return true;
    }

    private String getReachableUrl4Service(RequestContext ctx, String pathServiceName) {
        String reachableUrl;
        try {
            reachableUrl = this.customUrlAvailabilityService.getReachableUrl4Service(pathServiceName);
        } catch (Exception e) {
            log.error("[custom routing] failed to get reachable url. stop routing", e);
            ctx.setSendZuulResponse(false);

            HttpServletResponse response = ctx.getResponse();
            response.setStatus(500);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            try {
                response.getWriter().write(
                        String.format("failed to get reachable url of service %s", pathServiceName)
                );
            } catch (IOException ignored) {
            }
            return null;
        }
        return reachableUrl;
    }

    private URL transfer2UrlObj(RequestContext ctx, String reachableUrl) {
        URL routeHost;
        try {
            routeHost = new URL(reachableUrl);
        } catch (MalformedURLException e) {
            log.error("[custom routing] failed to resolve reachable url to URL object", e);
            ctx.setSendZuulResponse(false);

            HttpServletResponse response = ctx.getResponse();
            response.setStatus(500);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            try {
                response.getWriter().write(
                        String.format("failed to resolve reachable url( %s ) to URL object.", reachableUrl)
                );
            } catch (IOException ignored) {
            }
            return null;
        }
        return routeHost;
    }

    @Autowired
    public void setCustomRoutingServiceConfig(CustomRoutingServiceConfig customRoutingServiceConfig) {
        this.customRoutingServiceConfig = customRoutingServiceConfig;
    }

    @Autowired
    public void setCustomUrlAvailabilityService(CustomUrlAvailabilityService customUrlAvailabilityService) {
        this.customUrlAvailabilityService = customUrlAvailabilityService;
    }
}
