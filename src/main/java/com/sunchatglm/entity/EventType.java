package com.sunchatglm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: SunHB
 * @createTime: 2024/02/04 下午6:39
 * @description:
 */
@AllArgsConstructor
@Getter
public enum EventType {
    add("add","增加"),
    finish("finish","结束"),
    error("error","异常"),
    interrupt("interrupt","终止")
    ;
    private final String code;
    private final String info;

}
