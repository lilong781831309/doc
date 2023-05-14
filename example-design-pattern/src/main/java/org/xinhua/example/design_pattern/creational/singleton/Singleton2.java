package org.xinhua.example.design_pattern.creational.singleton;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 18:58
 * @Description: 懒汉式
 * @Version: 1.0
 */
public class Singleton2 {

    private Singleton2() {
    }

    private static Singleton2 instance;

    public synchronized static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }

}
