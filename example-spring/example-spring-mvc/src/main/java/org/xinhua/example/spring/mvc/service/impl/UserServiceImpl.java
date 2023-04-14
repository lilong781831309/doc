package org.xinhua.example.spring.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.xinhua.example.spring.mvc.model.po.User;
import org.xinhua.example.spring.mvc.repository.UserRepository;
import org.xinhua.example.spring.mvc.util.StringUtil;
import org.xinhua.example.spring.mvc.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User get(Long id) {
        if (id == null || id < 0) {
            return null;
        }
        Optional<User> optional = userRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public User get(String username) {
        if (StringUtil.isEmpty(username)) {
            return null;
        }
        List<User> users = userRepository.findByUsername(username);
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    @Override
    public Page<User> page(Integer pageNum, Integer pageSize) {
        Page<User> page = userRepository.findAll(PageRequest.of(pageNum, pageSize));
        return page;
    }

    @Override
    public List<User> all() {
        return userRepository.findAll();
    }

    public User add(User user) {
        user.setOrderFlag(0L);
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = get(id);
        if (user == null) {
            throw new RuntimeException("id不正确: " + id);
        }
        userRepository.deleteById(id);
    }

}
