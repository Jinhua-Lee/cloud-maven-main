package com.electric.cet.eurekaserver.domain;

import lombok.Data;

/**
 * @author Johnson Woo
 * @date: 2019/9/24 19:10
 * 封装结果的对象。
 */
@Data
public class Result<T> {

    // 返回码
    private int code;

    // 返回信息
    private String msg;

    // 返回数据
    private T data;

    public Result() {

    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
