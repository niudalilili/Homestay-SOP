package com.tanyde.service.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * RedisServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class RedisServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;
    @InjectMocks
    private RedisServiceImpl redisService;

    /**
     * 验证设置与读取键值的基础流程
     */
    @Test
    void shouldSetAndGetValue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("k1")).thenReturn("v1");

        redisService.set("k1", "v1", 10, TimeUnit.SECONDS);
        Object result = redisService.get("k1");

        verify(valueOperations).set("k1", "v1", 10, TimeUnit.SECONDS);
        assertEquals("v1", result);
    }

    /**
     * 验证匹配不到键时删除数量为 0
     */
    @Test
    void shouldReturnZeroWhenDeleteByPatternHasNoKeys() {
        when(redisTemplate.keys("p*")).thenReturn(Collections.emptySet());

        Long deleted = redisService.deleteByPattern("p*");

        assertEquals(0L, deleted);
    }

    /**
     * 验证按模式删除时返回正确删除数量
     */
    @Test
    void shouldDeleteKeysByPattern() {
        Set<String> keys = Set.of("a1", "a2");
        when(redisTemplate.keys("a*")).thenReturn(keys);
        when(redisTemplate.delete(keys)).thenReturn(2L);

        Long deleted = redisService.deleteByPattern("a*");

        assertEquals(2L, deleted);
    }
}
