package com.sunchatglm;

import com.sunchatglm.entity.*;
import com.sunchatglm.exception.APIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sunchatglm.util.JWTService;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author: SunHB
 * @createTime: 2024/01/28 下午8:50
 * @description:
 */

public class GLM {
    private final String apiKey;
    private String apiHost = Constant.API_HOST;
    protected OkHttpClient client;
    private ObjectMapper objectMapper = Constant.DEFAULT_OBJECT_MAPPER;

    private static final Logger logger = LoggerFactory.getLogger(GLM.class);
    public GLM(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 构建模型API调用 请求体
     * @param model
     * @param messages
     * @return
     */
    public GLMResponseBody askOriginal(String model, List<Message> messages){
        RequestBody body = RequestBody.create(buildRequestBody(model, messages), MediaType.get("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(apiHost)
                .addHeader("Authorization", JWTService.generateJWT(apiKey))
                .post(body)
                .build();
        client = new OkHttpClient.Builder().connectTimeout(20,TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build();
        try{
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                ResponseBody responseBody = response.body();
                throw new APIException("Request failed "+responseBody.toString());
            }else {
                assert response.body() != null;
                String bodyContent = response.body().string();
                return objectMapper.readValue(bodyContent,GLMResponseBody.class);
            }
        } catch (SocketTimeoutException ex){
            // 请求超时
            logger.info("ex:"+ex);
            throw new RuntimeException(ex);
        }catch (IOException e) {
            // IO 异常
            throw new RuntimeException(e);
        } catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }


    public String ask(String model,List<Message> messages){
        GLMResponseBody glmResponseBody = this.askOriginal(model, messages);
        List<GLMResponseBody.Choice> choices = glmResponseBody.getChoices();
        StringBuilder sb = new StringBuilder();
        for(GLMResponseBody.Choice choice:choices){
            sb.append(choice.getMessage().getContent());
        }
        return sb.toString();
    }

    public String ask(String model,String message){
        Message userMessage = Message.builder()
                .content(message)
                .role(Role.user.getCode())
                .build();
        return ask(model, Collections.singletonList(userMessage));
    }
    private String buildRequestBody(String model,List<Message> messages) {
        GLMRequestBody glmRequestBody = GLMRequestBody.builder()
                .model(model)
                .messages(messages)
                .build();
        try {
            return objectMapper.writeValueAsString(glmRequestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
