package org.xinhua.example.design_pattern.creational.factory_method.demo2;


/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:57
 * @Description: 迭代器接口  抽象产品
 * @Version: 1.0
 */
public interface Iterator<E> {

    boolean hasNext();

    E next();

    void remove();

}
