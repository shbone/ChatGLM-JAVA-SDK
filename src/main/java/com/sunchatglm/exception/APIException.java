package com.sunchatglm.exception;

/**
 * @author: SunHB
 * @createTime: 2024/02/03 上午10:09
 * @description:
 */
public class APIException extends RuntimeException{
    private Integer code;
    private String message;
    public APIException(Integer code ,String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public APIException(String message){
        this(400,message);

    }

}
