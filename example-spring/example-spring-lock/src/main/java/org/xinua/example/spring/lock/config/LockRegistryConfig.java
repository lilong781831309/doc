package org.xinua.example.spring.lock.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

@Data
@Configuration
public class LockRegistryConfig {

    @Value("${app.lock.prefix:lock}")
    private String prefix;

    @Value("${app.lock.default-expire-millis:60000}")
    private Long defaultExpireMillis;

    @Bean
    public LockRegistry lockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, prefix, defaultExpireMillis);
    }

}
