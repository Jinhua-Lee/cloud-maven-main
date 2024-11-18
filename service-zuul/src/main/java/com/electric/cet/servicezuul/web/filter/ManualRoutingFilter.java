package com.electric.cet.servicezuul.web.filter;

import com.electric.cet.servicezuul.config.ManualRoutingServerConfig;
import com.jinhua.feigncommon.util.CommonUtil;
import com.jinhua.feigncommon.util.NetStateUtil;
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
public class ManualRoutingFilter extends ZuulFilter {

    private ManualRoutingServerConfig manualRoutingServerConfig;

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
        RequestContext ctx = RequestContext.getCurrentContext();
        // a filter has already forwarded
        // a filter has already determined
        // return !ctx.containsKey(FORWARD_TO_KEY)
        //         && !ctx.containsKey(SERVICE_ID_KEY);
        // serviceId
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String uri = request.getRequestURI();
        log.debug("[manual routing] request uri: {}", uri);

        String pathServiceName = CommonUtil.getUriServiceName(uri);
        log.debug("[manual routing] service name: {}", pathServiceName);

        if (ObjectUtils.isEmpty(pathServiceName)) {
            log.warn("[manual routing] could not resolve service name from URI: {}", uri);
            return null;
        }

        // 判断是否在配置给定的手动路由服务范围内
        List<String> urls = Optional.ofNullable(manualRoutingServerConfig.getServices())
                .orElse(Collections.emptyMap())
                .get(pathServiceName);
        if (ObjectUtils.isEmpty(urls)) {
            log.debug("[manual routing] service name: {} not in manual routing service list",
                    pathServiceName
            );
            return null;
        }

        String reachableUrl = null;
        try {
            reachableUrl = NetStateUtil.getReachableUrl(urls, 10_000);
        } catch (Exception e) {
            log.error("[manual routing] failed to get reachable url. stop routing", e);
            ctx.setSendZuulResponse(false);

            HttpServletResponse response = ctx.getResponse();
            response.setStatus(500);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                response.getWriter().write(
                        String.format("failed to get reachable url of service %s", pathServiceName)
                );
            } catch (IOException ignored) {
            }
            return null;
        }
        log.debug("[manual routing] it's going to route to reachable url: {}", reachableUrl);
        URL routeHost = null;
        try {
            routeHost = new URL(reachableUrl);
        } catch (MalformedURLException e) {
            log.error("[manual routing] failed to resolve reachable url to URL object", e);
            ctx.setSendZuulResponse(false);

            HttpServletResponse response = ctx.getResponse();
            response.setStatus(500);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                response.getWriter().write(
                        String.format("failed to resolve reachable url( %s ) to URL object.", reachableUrl)
                );
            } catch (IOException ignored) {
            }
            return null;
        }
        ctx.setRouteHost(routeHost);
        ctx.setSendZuulResponse(true);
        return null;
    }

    @Autowired
    public void setManualRoutingServerConfig(ManualRoutingServerConfig manualRoutingServerConfig) {
        this.manualRoutingServerConfig = manualRoutingServerConfig;
    }
}
