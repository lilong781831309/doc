package org.xinhua.example.spring.cache.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter({RedisConfig.class})
public class CacheManagerConfig {

    @Autowired
    private CacheConfig cacheConfig;

    @Autowired
    private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

        //默认过期时间
        Long defaultTtl = cacheConfig.getDefaultTtl();
        //自定义过期时间
        Map<String, Long> customTtls = cacheConfig.getCustomTtls();
        //设置默认过期时间
        RedisCacheConfiguration defaultCacheConfigure = getCacheConfigure(Duration.ofSeconds(defaultTtl));
        //设置自定义过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        if (customTtls != null) {
            customTtls.forEach((cacheName, ttl) -> {
                if (ttl != null && ttl > 0) {
                    cacheConfigurationMap.put(cacheName, getCacheConfigure(Duration.ofSeconds(ttl)));
                }
            });
        }

        return new RedisCacheManager(redisCacheWriter, defaultCacheConfigure, cacheConfigurationMap);
    }


    private RedisCacheConfiguration getCacheConfigure(Duration duration) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(serializationPair())
                //.disableCachingNullValues()
                .computePrefixWith(cacheKeyPrefix());
    }

    @Bean
    RedisSerializationContext.SerializationPair<Object> serializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer);
    }

    @Bean
    CacheKeyPrefix cacheKeyPrefix() {
        return cacheName -> cacheConfig.getPrefix() + ":" + cacheName + ":";
    }

}