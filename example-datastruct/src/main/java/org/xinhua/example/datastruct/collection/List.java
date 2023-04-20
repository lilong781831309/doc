package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/21 5:33
 * @Description: 单列集合
 * @Version: 1.0
 */
public interface List<E> {

    E get(int index);

    E set(int index, E e);

    void add(E e);

    void add(int index, E e);

    void remove(E e);

    E removeAt(int index);

    int indexOf(E e);

    int size();

}
