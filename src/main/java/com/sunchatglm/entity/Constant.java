package com.sunchatglm.entity;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author: SunHB
 * @createTime: 2024/01/28 下午8:52
 * @description:
 */
public class Constant {
    Constant() {}
    // 智普Ai ChatGlM 请求地址
    public static String API_HOST = "https://open.bigmodel.cn/api/paas/v4/chat/completions";

    public static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static final Role DEFAULT_ROLE = Role.user;
}
