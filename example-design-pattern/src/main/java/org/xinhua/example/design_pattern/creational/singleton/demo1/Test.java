package org.xinhua.example.design_pattern.creational.singleton.demo1;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 18:32
 * @Description: 测试类
 * @Version: 1.0
 */
public class Test {

    public static void main(String[] args) throws Exception {
        serializable();
        reflect();
    }

    public static void serializable() throws Exception {
        Singleton instance = Singleton.getInstance();

        String path = "D:\\temp\\singleton6";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)));
        oos.writeObject(instance);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));
        Singleton instance2 = (Singleton) ois.readObject();
        ois.close();

        System.out.println("序列化:" + (instance == instance2));
    }

    public static void reflect() throws Exception {
        Singleton instance = Singleton.getInstance();

        Field field = Singleton.class.getDeclaredField("instanceExist");
        field.setAccessible(true);
        field.set(null, false);

        Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Singleton instance2 = constructor.newInstance();

        System.out.println("反射:" + (instance == instance2));
    }

}
