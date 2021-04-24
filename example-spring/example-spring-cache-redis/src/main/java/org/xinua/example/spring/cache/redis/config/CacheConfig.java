package org.xinua.example.spring.cache.redis.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.cache")
public class CacheConfig {

    private String prefix = "cache";
    private Long defaultTtl = 300L;
    private Map<String, Long> customTtls;

}