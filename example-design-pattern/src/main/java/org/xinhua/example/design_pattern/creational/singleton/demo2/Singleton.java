package org.xinhua.example.design_pattern.creational.singleton.demo2;

import java.io.Serializable;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 19:07
 * @Description: 静态内部类
 * @Version: 1.0
 */
public class Singleton implements Serializable {

    private static final long serialVersionUID = 2465363993281856421L;

    private Singleton() {
        synchronized (Singleton.class) {
            if (SingletonHolder.instanceExist) {
                throw new RuntimeException();
            }
            SingletonHolder.instanceExist = true;
        }
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static boolean instanceExist = false;
        private static final Singleton INSTANCE = new Singleton();
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
}
