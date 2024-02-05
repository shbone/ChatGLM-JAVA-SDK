package org.example;

import org.junit.jupiter.api.Test;
import com.sunchatglm.util.JWTService;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 上午11:12
 * @description:
 */
public class JwtTest {
    @Test
    public void testJwtGenerate(){
        String apiKey = "188107572495261cb3a30f93ad6e96c9.Kz6CVNsHaXjKqwxX";
        String jwt = JWTService.generateJWT(apiKey);
        System.out.println(jwt);
    }
}
