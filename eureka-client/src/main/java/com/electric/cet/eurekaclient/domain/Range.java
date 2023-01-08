package com.electric.cet.eurekaclient.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 数据取值范围
 */
@Data
public class Range {
    private Integer min;
    private Integer max;
    private Set<String> enumRange = new HashSet<>(50);

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
