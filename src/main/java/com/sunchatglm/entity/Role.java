package com.sunchatglm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 下午2:31
 * @description:
 */
@Getter
@AllArgsConstructor
public enum Role {
    user("user"),
    assistant("assistant"),
    system("system"),
    tool("tool");
    private final String code;

}
