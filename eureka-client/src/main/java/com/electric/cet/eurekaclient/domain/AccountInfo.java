/*
 * Copyright (c)    2019/10/17 下午3:25.
 * Author:    Jinhua-Work
 * PathName:    D:/IDEA_Projects/Learning/src/main/java/cn/diagram/AccountInfo.java
 * LastModified:    2019/10/17 下午3:25
 */

package com.electric.cet.eurekaclient.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 台账记录信息入参
 */
@Data
public class AccountInfo {

    // 数据记录的主键
    private Integer id;

    // 记录对应哪个台账
    private Integer accountId;

    // 信息版本号
    private Integer version;

    /**
     * 存储记录信息的包含属性与属性值的json格式字符串映射
     */
    private Map<String, Object> ext = new HashMap<>();

    private Object key;
}
