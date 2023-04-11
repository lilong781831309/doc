package org.xinua.example.datastruct.collection;

import java.util.concurrent.TimeUnit;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 17:47
 * @Description: 阻塞队列
 * @Version: 1.0
 */
public interface BlockQueue<E> extends Queue<E> {

    void put(E e) throws InterruptedException;


    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException;

    E take() throws InterruptedException;


    E poll(long timeout, TimeUnit unit) throws InterruptedException;
}
