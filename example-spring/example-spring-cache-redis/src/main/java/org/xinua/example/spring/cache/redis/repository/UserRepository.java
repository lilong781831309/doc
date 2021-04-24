package org.xinua.example.spring.cache.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xinua.example.spring.cache.redis.model.po.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String username);
}
