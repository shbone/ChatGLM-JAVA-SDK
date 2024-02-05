package com.sunchatglm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: SunHB
 * @createTime: 2024/01/28 下午9:08
 * @description: 模型API 请求体
 */
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestBody {
    /**
     * Required
     * <p>
     * 所要调用的模型编码
     */
    @JsonProperty("model")
    private String model;
    /**
     * Required
     * <p>
     * 调用语言模型时，将当前对话信息列表作为提示输入给模型
     */
    @JsonProperty("messages")
    private List<Message> messages;
    /**
     * Not Required
     * <p>
     * <p>
     * 由用户端传参，需保证唯一性；用于区分每次请求的唯一标识，用户端不传时平台会默认生成。
     */
    @JsonProperty("request_id")
    private String requestId = String.format("sun-%d", System.currentTimeMillis());
    /**
     * Not Reuqired
     * <p>
     * do_sample 为 true 时启用采样策略，do_sample 为 false 时采样策略 temperature、top_p 将不生效
     */

    private Boolean doSample = true;
    /**
     * Not Required
     * <p>
     * 使用同步调用时，此参数应当设置为 fasle 或者省略。表示模型生成完所有内容后一次性返回所有内容。
     */
    @JsonProperty("stream")
    private Boolean stream = false;
    /**
     * Not Reuqired
     * <p>
     * 采样温度，控制输出的随机性，必须为正数
     */
    @JsonProperty("temperature")
    private Float temperature = 0.9f;

    /**
     * Not Reuqired
     * <p>
     * 用温度取样的另一种方法，称为核取样
     * 取值范围是：(0.0, 1.0) 开区间，不能等于 0 或 1，默认值为 0.7
     */
    @JsonProperty("top_p")
    private Float topP = 0.7f;

    @Override
    public String toString() {
        Map<String, Object> paramsMap = new HashMap<>();
        if (null != this.model) {
            paramsMap.put("model", this.model);
        }
        if (null != this.requestId) {
            paramsMap.put("request_id", this.requestId);
        }
        if (null != this.doSample) {
            paramsMap.put("do_sample", this.doSample);
        }
        if (null != this.requestId) {
            paramsMap.put("request_id", this.requestId);
        }
        if (null != this.temperature) {
            paramsMap.put("temperature", this.temperature);
        }
        if (null != this.topP){
            paramsMap.put("top_p",this.topP);
        }
        if (null != this.messages){
            paramsMap.put("messages",this.messages);
        }
        if (null != this.stream){
            paramsMap.put("stream",this.stream);
        }
        try {
            return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
