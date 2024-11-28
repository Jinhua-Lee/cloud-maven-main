package com.electric.cet.servicezuul.service;

import java.util.Map;
import java.util.Set;

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

    /**
     * 获取所有可用的URL
     * @return 所有可用的URL
     */
    Map<String, Set<String>> getAllReachableUrlsByServiceName();
}
