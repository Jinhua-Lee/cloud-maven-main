package com.electric.cet.servicezuul.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
public class PostRoutingMsgLoggingFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "post";
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
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info("[post routing] ============ start ============");
        log.info("[post routing] remote host: {}", request.getRemoteHost());
        log.info("[post routing] remote port: {}", request.getRemotePort());
        log.info("[post routing] remote address: {}", request.getRemoteAddr());

        log.info("[post routing] server name: {}", request.getServerName());
        log.info("[post routing] server port: {}", request.getServerPort());
        log.info("[post routing] request url: {}", request.getRequestURL().toString());
        log.info("[post routing] ============ end ============");

        return null;
    }
}
