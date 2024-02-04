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
        String apiKey = "3e5ac0b282ad6ca3f1daa2e8376606b8.lxr0eqAQVCvu1SbI";
        String jwt = JWTService.generateJWT(apiKey);
        System.out.println(jwt);
    }
}
