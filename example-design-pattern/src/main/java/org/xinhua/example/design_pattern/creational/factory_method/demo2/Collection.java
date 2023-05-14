package org.xinhua.example.design_pattern.creational.factory_method.demo2;

import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 17:53
 * @Description: 抽象单列集合接口   抽象工厂
 * @Version: 1.0
 */
public interface Collection<E> {

    int size();

    E get(int index);

    void add(E e);

    void remove(E e);

    void remove(int index);

    void addAll(Collection<? extends E> collection);

    Iterator iterator();

    void forEach(Consumer<E> action);

}
