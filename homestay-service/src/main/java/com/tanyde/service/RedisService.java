package com.tanyde.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    void set(String key, Object value);
    void set(String key, Object value, long timeout, TimeUnit unit);
    Object get(String key);
    Boolean delete(String key);
    Boolean hasKey(String key);
    Boolean expire(String key, long timeout, TimeUnit unit);
    Long increment(String key, long delta);
    Long decrement(String key, long delta);
    void hset(String key, String field, Object value);
    Object hget(String key, String field);
    Map<Object, Object> hGetAll(String key);
    void hPutAll(String key, Map<String, Object> map);
    Long hDelete(String key, Object... fields);
    Long deleteByPattern(String pattern);


}
