/*
 * Copyright (c)    2019/10/17 下午2:46.
 * Author:    Jinhua-Work
 * PathName:    D:/IDEA_Projects/Learning/src/main/java/cn/diagram/Account.java
 * LastModified:    2019/10/17 下午2:46
 */

package com.electric.cet.eurekaserver.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 台账信息表，存储台账的名称和版本
 * 重写了 hashCode() 与 equals() 方法，判断是否为同一个台账的标准是 account_name。
 */
@Data
public class Account {
    /**
     * 台账的id
     */
    private Integer accountId;

    // 台账英文名
    private String accountName;

    // 台账中文名
    private String description;

    //台账的信息版本
    private Integer version;

    // 存储台账所包含的属性列
    private List<Property> properties = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(getAccountName(), account.getAccountName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountName());
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", properties=" + properties +
                '}';
    }
}
