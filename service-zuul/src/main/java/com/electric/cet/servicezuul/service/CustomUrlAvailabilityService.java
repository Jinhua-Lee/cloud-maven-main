package com.electric.cet.servicezuul.service;

/**
 * @author Jinhua-Lee
 */
public interface CustomUrlAvailabilityService {

    /**
     * 根据服务名获取可用的URL
     *
     * @param serviceName 服务名
     * @return 找到的可用URL
     */
    String getReachableUrl4Service(String serviceName);
}
