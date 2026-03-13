package com.tanyde.config;


import com.tanyde.json.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    //redis配置文件
    @Bean
    public RedisTemplate<String,Object>redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> template=new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        //使用自定的时间序列化方式
        GenericJackson2JsonRedisSerializer serializer=new GenericJackson2JsonRedisSerializer(new JacksonObjectMapper());

        //配置redis的key的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        //配置redis的hash的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        return template;
    }


}
