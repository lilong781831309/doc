package org.xinhua.example.design_pattern.creational.singleton;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 19:05
 * @Description: 双重检查锁
 * @Version: 1.0
 */
public class Singleton3 {

    private Singleton3() {
    }

    // 防止指令重排序
    private volatile static Singleton3 instance;

    public static Singleton3 getInstance() {
        // 读操作
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    // 非原子操作 1、为Singleton分配内存  2、Singleton实例对象初始化 3、实例对象地址赋值给instance
                    // 写操作
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }

}
