package org.xinhua.example.datastruct.map;

/**
 * @Author: lilong
 * @createDate: 2023/4/27 0:23
 * @Description: 迭代器
 * @Version: 1.0
 */
public interface Iterator<E> {

    boolean hasNext();

    E next();

    void remove();
}
