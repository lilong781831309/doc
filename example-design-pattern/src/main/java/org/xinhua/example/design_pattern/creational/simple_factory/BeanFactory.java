package org.xinhua.example.design_pattern.creational.simple_factory;

/**
 * @Author: lilong
 * @createDate: 2023/4/6 21:15
 * @Description: 创建实例对象的工厂
 * @Version: 1.0
 */
public interface BeanFactory {

    Object getBean(String id);

}
