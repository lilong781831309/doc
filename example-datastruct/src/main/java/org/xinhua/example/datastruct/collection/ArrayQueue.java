package org.xinhua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:18
 * @Description: 队列    数组实现
 * @Version: 1.0
 */
public class ArrayQueue<E> implements Queue<E> {

    protected int size;
    protected int head;
    protected int tail;
    protected Object[] elements = {};
    protected static final int DEFAULT_CAPACITY = 16;

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayQueue(int initCapacity) {
        if (initCapacity <= 0) {
            initCapacity = DEFAULT_CAPACITY;
        }
        elements = new Object[initCapacity];
    }

    @Override
    public boolean offer(E e) {
        if (full() && !newCapacity()) {
            return false;
        }
        elements[tail] = e;
        tail = inc(tail, elements.length);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (empty()) {
            return null;
        }
        E e = (E) elements[head];
        elements[head] = null;
        head = inc(head, elements.length);
        size--;
        return e;
    }

    @Override
    public E peek() {
        if (empty()) {
            return null;
        }
        return (E) elements[head];
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    public boolean full() {
        return size == elements.length;
    }

    protected int inc(int index, int modules) {
        index++;
        return index == modules ? 0 : index;
    }

    protected int dec(int index, int modules) {
        index--;
        return index == -1 ? modules - 1 : index;
    }

    protected boolean newCapacity() {
        if (size == Integer.MAX_VALUE) {
            return false;
        }

        int capacity = size << 1;

        if (capacity - Integer.MAX_VALUE > 0) {
            capacity = Integer.MAX_VALUE;
        }

        Object[] newElements = new Object[capacity];

        if (head < tail) {
            for (int i = 0, j = head; i < size; i++) {
                newElements[i] = elements[j];
            }
        } else {
            int index = 0;
            for (int i = head; i < elements.length; i++) {
                newElements[index] = elements[i];
                index++;
            }
            for (int i = 0; i < tail; i++) {
                newElements[index] = elements[i];
                index++;
            }
        }
        elements = newElements;
        head = 0;
        tail = size;

        return true;
    }

}
