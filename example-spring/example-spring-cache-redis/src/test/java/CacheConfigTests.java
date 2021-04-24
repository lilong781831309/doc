import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xinua.example.spring.cache.redis.ExampleApplication;
import org.xinua.example.spring.cache.redis.config.CacheConfig;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleApplication.class)
public class CacheConfigTests {

    @Autowired
    private CacheConfig cacheConfig;

    @Test
    public void test() throws Exception {
        String prefix = cacheConfig.getPrefix();
        Long defaultTtl = cacheConfig.getDefaultTtl();
        Map<String, Long> customTtls = cacheConfig.getCustomTtls();

        System.out.println(prefix);
        System.out.println(defaultTtl);
        System.out.println(customTtls);
    }

}
