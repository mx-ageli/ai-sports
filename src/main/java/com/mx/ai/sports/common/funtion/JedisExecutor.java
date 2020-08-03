package com.mx.ai.sports.common.funtion;

import com.mx.ai.sports.common.exception.RedisConnectException;

/**
 * Jedis处理类
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:22
 */
@FunctionalInterface
public interface JedisExecutor<T, R> {
    /**
     * 执行redis命令
     *
     * @param t
     * @return
     * @throws RedisConnectException
     */
    R excute(T t) throws RedisConnectException;
}
