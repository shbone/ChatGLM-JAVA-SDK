package com.sunchatglm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 上午9:30
 * @description: 模型响应体
 * 详见<p> https://open.bigmodel.cn/dev/api#glm-4 </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GLMResponseBody {
    /**
     * 任务Id
     */
    @JsonProperty("id")
    private String id;
    /**
     * 请求创建时间
     */
    @JsonProperty("created")
    private Long createdTime;

    /**
     * 模型名称
     */
    @JsonProperty("model")
    private String model;
    /**
     * 当前对话的模型输出内容
     */
    @JsonProperty("choices")
    private List<Choice> choices;
    /**
     * 结束时返回本次模型调用的 tokens 数量统计
     */
    @JsonProperty("usage")
    private TokenStatic usage;
    @Data
    public static class Choice {
        /**
         * 结果下标
         */
        @JsonProperty("index")
        private Integer index;
        /**
         * 模型推理终止的原因。
         *
         * stop代表推理自然结束或触发停止词。
         * tool_calls 代表模型命中函数。
         * length代表到达 tokens 长度上限。
         */
        @JsonProperty("finish_reason")
        private String finishReason;
        /**
         *
         * 模型返回的文本信息
         */
        @JsonProperty("message")
        private Message message;
    }
    @Data
    public static class TokenStatic{
        /**
         * 用户输入的 tokens 数量
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        /**
         *
         * 模型输入的 tokens 数量
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        /**
         * 总 tokens 数量
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }

}
