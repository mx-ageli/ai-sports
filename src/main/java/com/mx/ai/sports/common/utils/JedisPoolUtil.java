package com.mx.ai.sports.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Component
public class JedisPoolUtil {

    @Autowired
    private JedisPool jedisPool;

    private synchronized Jedis getJedis(){
        Jedis jedis = null;
        if(null != jedisPool){
            try {
                jedis = jedisPool.getResource();
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                returnResource(jedis);
            }
        }
        return jedis;
    }

    public synchronized void returnResource(Jedis jedis){
        if(null != jedis){
            jedis.close();
//            jedisPool.returnResource(jedis);
        }
    }

    public synchronized void returnBrokenResource(Jedis jedis){
        if(null != jedis){
            jedis.close();
//            jedisPool.returnBrokenResource(jedis);
        }
    }

    public List<String> hmGet(String hKey, String... keys){
        List<String> res = null;
        Jedis jedis = getJedis();
        try {
            res = jedis.hmget(hKey, keys);
        } catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }

    public String hGet(String hKey, String key){
        String res = null;
        Jedis jedis = getJedis();
        try {
            res = jedis.hget(hKey, key);
        } catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }

    public Long incrBy(String key, Long add){
        Long res = null;
        Jedis jedis = getJedis();
        try {
            res = jedis.incrBy(key, add);
        } catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }

    public long hSet(String hKey, String key, String value){
        long res = 0;
        Jedis jedis = getJedis();
        try {
            res = jedis.hset(hKey, key, value);
        }catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }

    public String get(String key){
        String res = null;
        Jedis jedis = getJedis();
        try {
            res = jedis.get(key);
        } catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }

    public void set(String key, String value){
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        }catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    public void del(String key){
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        }catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    public void hDel(String hKey, String key){
        Jedis jedis = getJedis();
        try {
            jedis.hdel(hKey, key);
        }catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 获取key的过期时间
     * @param key
     * @return
     */
    public Long ttl(String key){
        Long res = null;
        Jedis jedis = getJedis();
        try {
            res = jedis.ttl(key);
        } catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key){
        Boolean res = null;
        Jedis jedis = getJedis();
        try {
            res = jedis.exists(key);
        } catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }

    /**
     * 设置key失效时间
     * @param key
     * @return
     */
    public Long expire(String key, int seconds){
        Long res = null;
        Jedis jedis = getJedis();
        try {
            res = jedis.expire(key, seconds);
        } catch (Exception e){
            returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return res;
    }
}
