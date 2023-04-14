package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:20
 * @Description: 栈
 * @Version: 1.0
 */
public interface Stack<E> {

    boolean push(E e);

    E pop();

    E peek();

    boolean empty();

    int size();

}
