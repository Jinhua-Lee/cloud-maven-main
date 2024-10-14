package com.jinhua.feigncommon.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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
}