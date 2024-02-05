package org.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sunchatglm.model.GLM;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 下午12:38
 * @description:
 */
public class GLMApiTest {
    private Logger logger = LoggerFactory.getLogger(GLMApiTest.class);

    /**
     * 问答，同步调用
     */
    @Test
    public void testGLMApi(){
        String apikey = "YOUR_API_KEY";
        GLM glm = new GLM(apikey);
        String answer = glm.ask("glm-4", "你的中文怎么样");
        logger.info("answer:" + answer);
    }

    /**
     * 问答，SSE调用
     */
    @Test
    public void testGLMSSEApi(){
        String apikey = "YOUR_API_KEY";
        GLM glm = new GLM(apikey);
        String answer = glm.askSSE("glm-4", "华中科技大学在哪个城市");
    }
}
