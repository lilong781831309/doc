package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:18
 * @Description: 双端队列
 * @Version: 1.0
 */
public interface Deque<E> extends Queue<E> {

    boolean offerFirst(E e);

    boolean offerLast(E e);

    E pollFirst();

    E pollLast();

    E peekFirst();

    E peekLast();

}
