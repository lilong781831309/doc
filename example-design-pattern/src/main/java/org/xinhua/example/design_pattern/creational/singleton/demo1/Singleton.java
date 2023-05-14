package org.xinhua.example.design_pattern.creational.singleton.demo1;

import java.io.*;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 19:05
 * @Description: 双重检查锁
 * @Version: 1.0
 */
public class Singleton implements Serializable {

    private static final long serialVersionUID = -2402651294170731158L;
    // 防止指令重排序
    private volatile static Singleton instance;
    private static boolean instanceExist = false;

    private Singleton() {
        synchronized (Singleton.class) {
            if (instanceExist) {
                throw new RuntimeException();
            }
            instanceExist = true;
        }
    }

    public static Singleton getInstance() {
        // 读操作
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    // 非原子操作 1、为Singleton分配内存  2、Singleton实例对象初始化 3、实例对象地址赋值给instance
                    // 写操作
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    private Object readResolve() {
        return instance;
    }

}
