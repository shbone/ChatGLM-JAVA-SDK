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
    @Test
    public void testGLMApi(){
        String apikey = "188107572495261cb3a30f93ad6e96c9.Kz6CVNsHaXjKqwxX";
        GLM glm = new GLM(apikey);
        String answer = glm.ask("glm-4", "你好");
        logger.info("answer:" + answer);
    }
    @Test
    public void testGLMSSEApi(){
        String apikey = "188107572495261cb3a30f93ad6e96c9.Kz6CVNsHaXjKqwxX";
        GLM glm = new GLM(apikey);
        String answer = glm.askSSE("glm-4", "华中科技大学涂刚是谁？");
    }
}
