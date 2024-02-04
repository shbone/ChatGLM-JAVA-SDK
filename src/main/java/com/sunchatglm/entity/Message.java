package com.sunchatglm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 上午9:58
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role;
    private String content;
}
