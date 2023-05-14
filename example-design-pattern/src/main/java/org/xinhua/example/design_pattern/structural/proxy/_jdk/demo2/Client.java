package org.xinhua.example.design_pattern.structural.proxy._jdk.demo2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lilong
 * @createDate: 2023/4/9 1:09
 * @Description: 测试
 * @Version: 1.0
 */
public class Client {

    public static void main(String[] args) {
        //UserService userService = new UserServiceImpl();
        UserService userService = TransactionProxy.newProxyInstance(new UserServiceImpl());

        User user = userService.get(1L);
        user.setAddress("回龙观");
        user.setEmail("44444");

        User user2 = userService.get(2L);
        user2.setAddress("长安街");
        user2.setEmail("44444");

        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user2);

        userService.updateWithTransaction(list);
    }

}
