package com.mx.ai.sports.common.exception;

/**
 * Ekb系统内部异常
 * @author Mengjiaxin
 * @date 2019-08-20 16:20
 */
public class AiSportsException extends RuntimeException {


    private static final long serialVersionUID = 6086923613895968276L;

    public AiSportsException(String message) {

        super(message);
    }

    public AiSportsException(String message, boolean writableStackTrace){
        super(message, null ,false, writableStackTrace);
    }

}
