package org.xinua.example.spring.lock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LockRegistry lockRegistry;

    @Override
    public AccessToken getToken() {
        AccessToken token = getCachedToken();
        //缓存中没有或过期了,获取新token并放入缓存
        if (token == null) {
            Lock lock = lockRegistry.obtain("tokenLock");
            try {
                lock.tryLock(3, TimeUnit.SECONDS);
                //判断有没有其他线程获取了token
                token = getCachedToken();
                if (token == null) {
                    token = newToken();
                    cacheToken(token);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        return token;
    }

    private AccessToken getCachedToken() {
        return (AccessToken) redisTemplate.boundValueOps("tokenKey").get();
    }

    private void cacheToken(AccessToken accessToken) {
        long expires_in = accessToken.getExpires_in();
        //一般token为一小时或两小时有效,如果有上传下载等长时间任务,提前5分钟失效
        long expires_at = System.currentTimeMillis() + expires_in - 5 * 60 * 1000L;
        redisTemplate.delete("tokenKey");
        redisTemplate.boundValueOps("tokenKey").set(accessToken);
        redisTemplate.boundValueOps("tokenKey").expireAt(new Date(expires_at));
    }

    @Override
    public AccessToken newToken() {
        AccessToken accessToken = new AccessToken();
        // appId appSecret http请求获取token
        accessToken.setAccess_token("1000000" + new Random().nextInt(10000000));
        accessToken.setExpires_in(6 * 60 * 1000L);

        return accessToken;
    }


}
