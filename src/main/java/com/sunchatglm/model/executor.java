package com.sunchatglm.model;

import com.sunchatglm.entity.ChatRequestBody;
import com.sunchatglm.entity.ChatResponseStreamBody;
import com.sunchatglm.entity.ChatResponseSyncBody;
import okhttp3.sse.EventSource;

import java.io.IOException;

/**
 * @author: SunHB
 * @createTime: 2024/02/05 上午10:44
 * @description:
 */
public interface executor {
    ChatResponseSyncBody chatCompletionSync(ChatRequestBody GLMchatRequestBody) throws Exception;
    ChatResponseStreamBody chatCompletionSSE(ChatRequestBody GLMchatRequestBody) throws IOException;
}
