package org.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sunchatglm.GLM;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 下午12:38
 * @description:
 */
public class GLMSDKTest {
    private Logger logger = LoggerFactory.getLogger(GLMSDKTest.class);
    @Test
    public void testGLMApi(){
        String apikey = "YOUR_API_KEY";
        GLM glm = new GLM(apikey);
        String answer = glm.ask("glm-4", "你好");
        logger.info("answer:" + answer);
    }
}
