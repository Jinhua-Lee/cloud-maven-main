package com.jinhua.feigncommon.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author laurent
 * @date 2021/07/14 16:42
 */
@Slf4j
public class NetStateUtil {

    /**
     * 通过IP和端口判断主机是否可达
     *
     * @param address       IP地址
     * @param openPort      端口
     * @param timeoutMillis 超时时间
     * @return 是否可达
     */
    public static boolean isReachable(String address, int openPort, int timeoutMillis) {
        if (!CommonUtil.isIpAddress(address)) {
            throw new IllegalArgumentException("pecnode.ip.check.error");
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(address, openPort), timeoutMillis);
            log.debug("ip {} port {} is reachable", address, openPort);
            return true;
        } catch (IOException ex) {
            log.debug("ip {} port {} is unreachable, error message: ", address, openPort, ex);
            return false;
        }
    }

    public static String getReachableUrl(List<String> urlList, int testConnectTimeout) {
        List<String> finalUrlList = Optional.ofNullable(
                urlList
        ).orElse(Collections.emptyList());
        for (String url : finalUrlList) {
            String ip = resolveIp(url);
            Integer port = resolvePort(url);
            if (isReachable(ip, port, testConnectTimeout)) {
                return url;
            }
        }
        throw new IllegalStateException(
                String.format("no url is reachable. urlList = %s", urlList)
        );
    }

    /**
     * 从给定URL中解析出IP
     *
     * @param configUrl 配置的URL
     * @return ConfigServer的IP
     */
    public static String resolveIp(String configUrl) {
        int start = configUrl.indexOf("://");
        int end = configUrl.lastIndexOf(":");
        int first = configUrl.indexOf(":");

        if (start == -1 || end == -1 || first == end) {
            throw new IllegalStateException(
                    String.format("invalid url = %s", configUrl)
            );
        }
        return configUrl.substring(start + 3, end);
    }

    public static Integer resolvePort(String url) {
        return Integer.parseInt(url.substring(url.lastIndexOf(":") + 1));
    }
}