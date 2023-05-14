package org.xinhua.example.design_pattern.creational.singleton;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 18:31
 * @Description: 饿汉式
 * @Version: 1.0
 */
public class Singleton1 {

    private Singleton1() {
    }

    private static Singleton1 instance = new Singleton1();

    public static Singleton1 getInstance() {
        return instance;
    }

}
