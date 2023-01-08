/*
 * Copyright (c)    2019/10/17 下午2:49.
 * Author:    Jinhua-Work
 * PathName:    D:/IDEA_Projects/Learning/src/main/java/cn/diagram/Property.java
 * LastModified:    2019/10/17 下午2:49
 */

package com.electric.cet.eurekaserver.domain;

import lombok.Data;

import java.util.Objects;

/**
 * 台账属性列
 * 重写了 hashCode() 与 equals() 方法，判断是否为同一个属性的依据是 account_id 和 property_name 是否相同
 */
@Data
public class Property {

    // 属性列id
    private Integer propertyId;

    // 对应哪个台账
    private Integer accountId;

    // 属性列英文名
    private String propertyName;

    // 属性列中文名
    private String description;

    // 属性列取值范围
    private Range range;

    // 引用的属性数据类型
    private Integer datatypeId;

    // 该字段是否为主键, 1为true, 0为false
    private Integer key;

    @Override
    public String toString() {
        return "Property{" +
                "propertyId=" + propertyId +
                ", accountId=" + accountId +
                ", propertyName='" + propertyName + '\'' +
                ", description='" + description + '\'' +
                ", range=" + range +
                ", datatypeId=" + datatypeId +
                ", key=" + key +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Property property = (Property) o;
        return Objects.equals(getAccountId(), property.getAccountId()) &&
                Objects.equals(getPropertyName(), property.getPropertyName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getPropertyName());
    }
}
