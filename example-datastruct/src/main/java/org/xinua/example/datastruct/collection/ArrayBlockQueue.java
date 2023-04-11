package org.xinua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 17:38
 * @Description: 阻塞队列  数组实现
 * @Version: 1.0
 */
public class ArrayBlockQueue<E> extends AbstrackBlockQueue<E> {

    protected int head;

    protected int tail;

    protected Object[] elements = {};

    public ArrayBlockQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayBlockQueue(int initCapacity) {
        super(initCapacity);
        elements = new Object[initCapacity];
    }

    @Override
    protected void enQueue(E e) {
        elements[tail] = e;
        tail = inc(tail, elements.length);
    }

    @Override
    protected E deQueue() {
        E e = (E) elements[head];
        elements[head] = null;
        head = inc(head, elements.length);
        return e;
    }

    @Override
    public E peek() {
        takeLock.lock();
        try {
            if (empty()) {
                return null;
            }
            return (E) elements[head];
        } finally {
            takeLock.unlock();
        }
    }

    private int inc(int index, int modules) {
        index++;
        return index == modules ? 0 : index;
    }

    private int dec(int index, int modules) {
        index--;
        return index == -1 ? modules - 1 : index;
    }

}
