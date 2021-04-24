package org.xinua.example.spring.cache.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer());
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer());
        // valuevalue采用jackson序列化方式
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        // hash的value采用jackson序列化方式
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer(Object.class);
    }

}