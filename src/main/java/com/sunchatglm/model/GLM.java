package com.sunchatglm.model;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunchatglm.entity.*;
import com.sunchatglm.exception.APIException;
import com.sunchatglm.util.JWTService;
import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * @author: SunHB
 * @createTime: 2024/01/28 下午8:50
 * @description:
 */

public class GLM implements executor {
    private final String apiKey;
    private String apiHost = Constant.API_HOST;
    protected OkHttpClient client;
    private ObjectMapper objectMapper = Constant.DEFAULT_OBJECT_MAPPER;

    private static final Logger logger = LoggerFactory.getLogger(GLM.class);

    public GLM(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 问答API调用， 同步请求
     * @param chatRequestBody
     * @return
     */
    @Override
    public ChatResponseSyncBody chatCompletionSync(ChatRequestBody chatRequestBody) {
        //Request request;
        //RequestBody body = null;
        //try {
        //    body = RequestBody.create(MediaType.get("application/json;charset=utf-8"), objectMapper.writeValueAsString(chatRequestBody));
        //} catch (JsonProcessingException e) {
        //    throw new RuntimeException(e);
        //}
        // 构建request请求
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), chatRequestBody.toString());
        Request request = new Request.Builder()
                .post(body)
                .url(apiHost)
                .addHeader("Authorization", JWTService.generateJWT(apiKey))
                .build();
        client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                ResponseBody responseBody = response.body();
                throw new APIException("Request failed " + responseBody.toString());
            }else {
                assert response.body() != null;
                String bodyContent = response.body().string();
                return objectMapper.readValue(bodyContent, ChatResponseSyncBody.class);
            }
        } catch (SocketTimeoutException ex) {
            // 请求超时
            logger.info("Socket Time Out Exception:" + ex);
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            // IO 异常
            logger.info("IO Exception:" + ex);
            throw new RuntimeException(ex);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * 问答API调用，流式请求
     * @param chatRequestBody 发送请求
     * @param
     * @return
     */

    public ChatResponseStreamBody chatCompletionSSE(ChatRequestBody chatRequestBody){

        Request request = new Request.Builder()
                .addHeader("Authorization", JWTService.generateJWT(apiKey))
                .url(apiHost)
                .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), chatRequestBody.toString()))
                .build();
        client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        // 锁，等待所有流式响应发送完毕后关闭
        CountDownLatch countDownLatch = new CountDownLatch(1);
        RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                if ("[DONE]".equals(data)) {
                    return;
                }
                ChatResponseStreamBody response = JSON.parseObject(data, ChatResponseStreamBody.class);
                //  终止
                if (response.getChoices() != null && 1 == response.getChoices().size() && "stop".equals(response.getChoices().get(0).getFinishReason())) {
                    return;
                }
                logger.info("测试结果 onEvent：{}", ((ChatResponseStreamBody.Delta)response.getChoices().get(0).getDelta()).getContent());
            }

            @Override
            public void onClosed(EventSource eventSource) {
                logger.error("event on close");
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                logger.info("event on fail");
                countDownLatch.countDown();
            }
        });
        realEventSource.connect(client);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    /**
     * 构建模型API调用 基础请求体
     *
     * @param model
     * @param messages
     * @return
     */
    @Deprecated
    public ChatResponseSyncBody askOriginal(String model, List<Message> messages) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),buildRequestBody(model, messages));
        Request request = new Request.Builder()
                .url(apiHost)
                .addHeader("Authorization", JWTService.generateJWT(apiKey))
                .post(body)
                .build();
        client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                throw new APIException("Request failed " + responseBody.toString());
            } else {
                assert response.body() != null;
                String bodyContent = response.body().string();
                return objectMapper.readValue(bodyContent, ChatResponseSyncBody.class);
            }
        } catch (SocketTimeoutException ex) {
            // 请求超时
            logger.info("Socket Time Out Exception:" + ex);
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            // IO 异常
            logger.info("IO Exception:" + ex);
            throw new RuntimeException(ex);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    public String ask(String model, List<Message> messages) {
        ChatRequestBody glMchatRequestBody = ChatRequestBody.builder()
                .messages(messages)
                .model(model)
                .stream(false)
                .build();
        ChatResponseSyncBody glmResponseBody = this.chatCompletionSync(glMchatRequestBody);
        List<ChatResponseSyncBody.Choice> choices = glmResponseBody.getChoices();
        StringBuilder sb = new StringBuilder();
        for (ChatResponseSyncBody.Choice choice : choices) {
            sb.append(choice.getMessage().getContent());
        }
        return sb.toString();
    }

    public String ask(String model, String message) {
        Message userMessage = Message.builder()
                .content(message)
                .role(Role.user.getCode())
                .build();
        return ask(model, Collections.singletonList(userMessage));
    }

    public String askSSE(String model,String message){
        Message userMessage = Message.builder()
                .content(message)
                .role(Role.user.getCode())
                .build();
        return askSSE(model, Collections.singletonList(userMessage));
    }

    public String askSSE(String model,List<Message> messages){
        ChatRequestBody glMchatRequestBody = ChatRequestBody.builder()
                .messages(messages)
                .model(model)
                .stream(true)
                .build();

        this.chatCompletionSSE(glMchatRequestBody);
        return null;
    }
    /**
     * 构建request请求体
     *
     * @param model
     * @param messages
     * @return
     */
    private String buildRequestBody(String model, List<Message> messages) {
        ChatRequestBody chatRequestBody = ChatRequestBody.builder()
                .model(model)
                .messages(messages)
                .build();
        try {
            return objectMapper.writeValueAsString(chatRequestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
