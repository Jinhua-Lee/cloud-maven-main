package com.jinhua.eurekaclient.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Jinhua-Lee
 */
@Slf4j
public class RemoteMsgFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("[routing from] ============ start ============");
        log.info("[routing from] remote host: {}", request.getRemoteHost());
        log.info("[routing from] remote port: {}", request.getRemotePort());
        log.info("[routing from] remote address: {}", request.getRemoteAddr());

        log.info("[routing from] server name: {}", request.getServerName());
        log.info("[routing from] server port: {}", request.getServerPort());
        HttpServletRequest req = (HttpServletRequest) request;
        log.info("[routing from] request url: {}", req.getRequestURL().toString());
        log.info("[routing from] ============ end ============");
        chain.doFilter(request, response);
    }
}
