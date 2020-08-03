package com.mx.ai.sports.common.exception;

/**
 * Redis 连接异常
 * @author Mengjiaxin
 * @date 2019-08-20 16:21
 */
public class RedisConnectException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public RedisConnectException(String message) {
        super(message);
    }
}
