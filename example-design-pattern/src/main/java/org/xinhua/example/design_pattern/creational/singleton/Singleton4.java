package org.xinhua.example.design_pattern.creational.singleton;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 19:07
 * @Description: 静态内部类
 * @Version: 1.0
 */
public class Singleton4 {

    private Singleton4() {
    }

    public static Singleton4 getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Singleton4 INSTANCE = new Singleton4();
    }

}
