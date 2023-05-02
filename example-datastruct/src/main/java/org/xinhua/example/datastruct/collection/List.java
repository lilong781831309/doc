package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/21 5:33
 * @Description: 单列集合
 * @Version: 1.0
 */
public interface List<E> extends Collection<E> {

    E get(int index);

    E set(int index, E e);

    boolean add(E e);

    boolean add(int index, E e);

    void addAll(List<E> list);

    boolean remove(E e);

    E removeAt(int index);

    void removeBetween(int start, int end);

    int indexOf(E e);

    int size();

}
