package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.util.List;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 2:04
 * @Description: 用户服务接口
 * @Version: 1.0
 */
public interface UserService {

    User get(Long id);

    void update(List<User> list);

    void updateWithTransaction(List<User> list);

}
