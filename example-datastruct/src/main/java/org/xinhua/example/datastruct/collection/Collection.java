package org.xinhua.example.datastruct.collection;


import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/27 0:22
 * @Description: 集合
 * @Version: 1.0
 */
public interface Collection<E> {

    int size();

    boolean isEmpty();

    boolean contains(E e);

    Iterator<E> iterator();

    boolean add(E e);

    boolean remove(E e);

    boolean containsAll(Collection<E> c);

    boolean addAll(Collection<E> c);

    boolean removeAll(Collection<E> c);

    void clear();

    void forEach(Consumer<? super E> action);

}
