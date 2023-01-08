/*
 * Copyright (c)    2019/10/18 上午10:12.
 * Author:    Jinhua-Work
 * PathName:    D:/IDEA_Projects/Learning/src/main/java/org/PageBean.java
 * LastModified:    2019/10/18 上午10:12
 */

package com.electric.cet.eurekaclient.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PageBean<T> {

    // 台账id
    private Integer accountId;

    // 当前页数
    private Integer currPage;

    // 每页显示记录数
    private Integer pageSize;

    // 总记录数
    private Integer totalCount = 0;

    // 总页数
    private Integer totalPage;

    // 查询条件
    private Map<String, Object> ext;

    // 存储的记录列表
    private List<T> results = new ArrayList<>();

    public PageBean() {

    }

    /**
     * 构造函数
     * 四个参数中这三个需要手动注入，总页数是计算出来的
     * 只提供 getter ， 不提供 setter ，构造函数行使了 setter 的功能
     * @param currPage 当前页数
     * @param pageSize 每页显示记录数
     * @param totalCount 总记录数
     */
    public PageBean(Integer currPage, Integer pageSize, Integer totalCount) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    /**
     * 每次查询完成，执行参数设置
     */
    public void dealWithParam() {
        // 对总页数的处理
        if (totalCount % pageSize == 0) {   // 若总记录数是每页显示记录的整数倍
            totalPage = totalCount / pageSize;
        }
        else {  // 不是整数倍，还需一页来存储
            totalPage = totalCount / pageSize + 1;
        }

        // 对当前页的处理
        if (this.currPage <= 0) {
            this.currPage = 1;
        }
        if (this.currPage >= totalPage + 1) {
            this.currPage = totalPage;
        }
    }
}
