package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.util.List;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 2:04
 * @Description: 用户服务接口实现类
 * @Version: 1.0
 */
public class UserServiceImpl implements UserService {

    //private UserDao dao = new UserDaoImpl();
    private UserDao dao = TransactionProxy.newProxyInstance(new UserDaoImpl());

    @Override
    public User get(Long id) {
        return dao.get(id);
    }

    @Override
    public void update(List<User> list) {
        list.forEach(dao::update);
    }

    @Transaction
    @Override
    public void updateWithTransaction(List<User> list) {
        list.forEach(dao::update);
    }

}
