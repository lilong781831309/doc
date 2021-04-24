package org.xinua.example.spring.mvc.service;

import org.springframework.data.domain.Page;
import org.xinua.example.spring.mvc.model.po.User;

import java.util.List;

public interface UserService {

    User get(Long id);

    User get(String username);

    Page<User> page(Integer pageNum, Integer pageSize);

    List<User> all();

    User add(User user);

    User update(User user);

    void delete(Long id);
}
