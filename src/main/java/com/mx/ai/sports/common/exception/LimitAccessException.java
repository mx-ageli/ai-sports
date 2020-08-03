package com.mx.ai.sports.common.exception;

/**
 * 限流异常
 * @author Mengjiaxin
 * @date 2019-08-20 16:20
 */
public class LimitAccessException extends Exception {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException(String message) {
        super(message);
    }
}