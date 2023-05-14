package org.xinhua.example.design_pattern.creational.simple_factory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 21:17
 * @Description: 工厂方法实现类
 * @Version: 1.0
 */
public class SimpleBeanFactory implements BeanFactory {

    private Map<String, String> classNameMap = new ConcurrentHashMap();
    private Map<String, Object> beanMap = new ConcurrentHashMap();

    public SimpleBeanFactory() {
        initClassName();
    }


    private void initClassName() {
        Properties properties = new Properties();
        InputStream is = SimpleBeanFactory.class.getResourceAsStream("bean.properties");
        try {
            properties.load(is);
            properties.forEach((id, className) -> {
                classNameMap.put((String) id, (String) className);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        Object object = beanMap.get(id);
        if (object != null) {
            return object;
        }

        String className = classNameMap.get(id);
        if (className == null) {
            return null;
        }

        try {
            Class<?> clazz = Class.forName(className);
            object = clazz.getDeclaredConstructor().newInstance();
            beanMap.put(id, object);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return object;
    }
}
