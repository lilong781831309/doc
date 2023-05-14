package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.util.List;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 0:08
 * @Description: 用户Dao
 * @Version: 1.0
 */
public interface UserDao {

    User get(Long id);

    List<User> getAll();

    User add(User user);

    User update(User user);

    void delete(Long id);

}
