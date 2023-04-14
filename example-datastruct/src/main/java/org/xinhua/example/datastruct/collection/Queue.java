package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:18
 * @Description: 队列
 * @Version: 1.0
 */
public interface Queue<E> {

    boolean offer(E e);

    E poll();

    E peek();

    boolean empty();

    int size();

}
