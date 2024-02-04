package com.sunchatglm.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sunchatglm.exception.APIException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 上午10:11
 * @description:
 */
public class JWTService {


    // JWT的有效时间，这里设为30分钟
    private static final long EXPIRATION_TIME = 30 * 60L * 1000;
    // cache 缓存 JWT
    private static Cache<String,String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(EXPIRATION_TIME, TimeUnit.MILLISECONDS).build();

    public static String generateJWT(String apiKey) {

        // 校验apiKey
        String[] splits = apiKey.split("\\.");
        if (splits == null || splits.length != 2) {
            throw new APIException("Your apikey is invalid");
        }

        String apiId = splits[0];
        String apiSecret = splits[1];
        String token;
        //查找缓存
        token = cache.getIfPresent(apiId);
        if(token != null ){
            return token;
        }

        Algorithm algorithm = Algorithm.HMAC256(apiSecret.getBytes());
        Map<String, Object> payload = new HashMap<String, Object>();
        Map<String, Object> header = new HashMap<String, Object>();

        payload.put("api_key", apiId);
        payload.put("exp", System.currentTimeMillis() + EXPIRATION_TIME);
        payload.put("timestamp", System.currentTimeMillis());

        header.put("alg", "HS256");
        header.put("sign_type", "SIGN");
        token = JWT.create().withPayload(payload).withHeader(header).sign(algorithm);
        cache.put(apiId,token);
        return token;
    }
}
