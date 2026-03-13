package com.tanyde.service.Impl;

import com.tanyde.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //设置键值对
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }
    //设置键值对并设置过期时间
    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key,value,timeout,unit);
    }
    //获取键对应的值
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    //删除键对应的值
    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
    //判断键是否存在
    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
    //设置键的过期时间
    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key,timeout,unit);
    }
    //键自增
    @Override
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key,delta);
    }
    //键自减
    @Override
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key,delta);
    }
    //设置键值对
    @Override
    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key,field,value);
    }
    //获取键对应的值
    @Override
    public Object hget(String key, String field) {
        return redisTemplate.opsForHash().get(key,field);
    }
    //获取键对应的所有值
    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
    //批量设置键值对
    @Override
    public void hPutAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key,map);
    }
    //删除键对应的值
    @Override
    public Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key,fields);
    }
    //按照匹配的删除
    @Override
    public Long deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) {
            return 0L;
        }
        return redisTemplate.delete(keys);
    }
}
