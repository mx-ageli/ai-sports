package com.mx.ai.sports.monitor.service;


import com.mx.ai.sports.common.exception.RedisConnectException;
import com.mx.ai.sports.monitor.entity.RedisInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis的Service
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:54
 */
public interface IRedisService {

    /**
     * 获取 redis 的详细信息
     *
     * @return List
     * @throws RedisConnectException
     */
    List<RedisInfo> getRedisInfo() throws RedisConnectException;

    /**
     * 获取 redis key 数量
     *
     * @return Map
     * @throws RedisConnectException
     */
    Map<String, Object> getKeysSize() throws RedisConnectException;

    /**
     * 获取 redis 内存信息
     *
     * @return Map
     * @throws RedisConnectException
     */
    Map<String, Object> getMemoryInfo() throws RedisConnectException;

    /**
     * 获取 key
     *
     * @param pattern 正则
     * @return Set
     * @throws RedisConnectException
     */
    Set<String> getKeys(String pattern) throws RedisConnectException;

    /**
     * get命令
     *
     * @param key key
     * @return String
     * @throws RedisConnectException
     */
    String get(String key) throws RedisConnectException;

    /**
     * set命令
     *
     * @param key   key
     * @param value value
     * @return String
     * @throws RedisConnectException
     */
    String set(String key, String value) throws RedisConnectException;

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param milliscends 毫秒
     * @return String
     * @throws RedisConnectException
     */
    String set(String key, String value, Long milliscends) throws RedisConnectException;

    /**
     * del命令
     *
     * @param key key
     * @return Long
     * @throws RedisConnectException
     */
    Long del(String... key) throws RedisConnectException;

    /**
     * exists命令
     *
     * @param key key
     * @return Boolean
     * @throws RedisConnectException
     */
    Boolean exists(String key) throws RedisConnectException;

    /**
     * pttl命令
     *
     * @param key key
     * @return Long
     * @throws RedisConnectException
     */
    Long pttl(String key) throws RedisConnectException;

    /**
     * pexpire命令
     *
     * @param key         key
     * @param milliscends 毫秒
     * @return Long
     * @throws RedisConnectException
     */
    Long pexpire(String key, Long milliscends) throws RedisConnectException;


    /**
     * zadd 命令
     *
     * @param key    key
     * @param score  score
     * @param member value
     * @return Long
     * @throws RedisConnectException
     */
    Long zadd(String key, Double score, String member) throws RedisConnectException;

    /**
     * zrangeByScore 命令
     *
     * @param key key
     * @param min min
     * @param max max
     * @return Set<String>
     * @throws RedisConnectException
     */
    Set<String> zrangeByScore(String key, String min, String max) throws RedisConnectException;

    /**
     * zremrangeByScore 命令
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return Long
     * @throws RedisConnectException
     */
    Long zremrangeByScore(String key, String start, String end) throws RedisConnectException;

    /**
     * zrem 命令
     *
     * @param key     key
     * @param members members
     * @return Long
     * @throws RedisConnectException
     */
    Long zrem(String key, String... members) throws RedisConnectException;
}
