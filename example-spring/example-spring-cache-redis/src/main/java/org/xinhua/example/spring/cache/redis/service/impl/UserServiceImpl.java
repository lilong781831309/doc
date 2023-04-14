package org.xinhua.example.spring.cache.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.xinhua.example.spring.cache.redis.model.po.User;
import org.xinhua.example.spring.cache.redis.util.StringUtil;
import org.xinhua.example.spring.cache.redis.repository.UserRepository;
import org.xinhua.example.spring.cache.redis.service.UserService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * 自定义注解,组合多个注解
     */
    @Caching(
            put = {
                    @CachePut(value = "user_id", key = "#user.id", condition = "#user != null && #user.id != null && #user.id > 0"),
                    @CachePut(value = "user_username", key = "#user.username", condition = "#user != null && #user.username != null")
            },
            evict = {
                    @CacheEvict(value = "user_page", allEntries = true),
                    @CacheEvict(value = "user_all", allEntries = true),
            }
    )
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface UserUpdateCache {
    }

    @Cacheable(value = "user_id", key = "#id", condition = "#id != null && #id > 0", unless = "#result == null")
    @Override
    public User get(Long id) {
        if (id == null || id < 0) {
            return null;
        }
        Optional<User> optional = userRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    @Cacheable(value = "user_username", key = "#username", condition = "#username != null", unless = "#result == null")
    @Override
    public User get(String username) {
        if (StringUtil.isEmpty(username)) {
            return null;
        }
        List<User> users = userRepository.findByUsername(username);
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    @Cacheable(value = "user_page", key = "#pageNum + '-' + #pageSize", condition = "#pageNum != null && #pageNum >= 0 && #pageSize != null && #pageSize > 0", unless = "#result == null || #result.size() == 0")
    @Override
    public Page<User> page(Integer pageNum, Integer pageSize) {
        Page<User> page = userRepository.findAll(PageRequest.of(pageNum, pageSize));
        return page;
    }

    @Cacheable(value = "user_all", unless = "#result == null || #result.size() == 0")
    @Override
    public List<User> all() {
        return userRepository.findAll();
    }

    @UserUpdateCache
    @Override
    public User add(User user) {
        user.setOrderFlag(0L);
        return userRepository.save(user);
    }

    @UserUpdateCache
    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "user_id", key = "#id"),
                    @CacheEvict(value = "user_username", allEntries = true),
                    @CacheEvict(value = "user_page", allEntries = true),
                    @CacheEvict(value = "user_all", allEntries = true),
            }
    )
    @Override
    public void delete(Long id) {
        User user = get(id);
        if (user == null) {
            throw new RuntimeException("id不正确: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Map<Long, User> map() {
        Map<Long, User> map = new LinkedHashMap<>();
        //内部方法调用默认不走缓存,通过代理类才能走缓存
        List<User> all = userService.all();
        all.forEach((user) -> {
            map.put(user.getId(), user);
        });
        return map;
    }

}
