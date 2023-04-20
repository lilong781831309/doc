package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:31
 * @Description: 双端队列    数组实现
 * @Version: 1.0
 */
public class ArrayDeQueue<E> extends ArrayQueue<E> implements Deque<E> {

    public ArrayDeQueue() {
        super();
    }

    public ArrayDeQueue(int initCapacity) {
        super(initCapacity);
    }

    @Override
    public boolean offerFirst(E e) {
        if (full() && !ensureCapacity(size + 1)) {
            return false;
        }
        head = dec(head, elements.length);
        elements[head] = e;
        size++;
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        return super.offer(e);
    }

    @Override
    public E pollFirst() {
        return super.poll();
    }

    @Override
    public E pollLast() {
        if (empty()) {
            return null;
        }
        tail = dec(tail, elements.length);
        E e = (E) elements[tail];
        elements[tail] = null;
        size--;
        return e;
    }

    @Override
    public E peekFirst() {
        return super.peek();
    }

    @Override
    public E peekLast() {
        if (empty()) {
            return null;
        }
        int index = dec(tail, elements.length);
        return (E) elements[index];
    }

}
