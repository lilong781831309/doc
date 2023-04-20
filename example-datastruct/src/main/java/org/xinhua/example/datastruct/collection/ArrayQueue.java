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
    }

    public ArrayQueue(int initCapacity) {
        if (initCapacity > 0) {
            elements = new Object[initCapacity];
        }
    }

    @Override
    public boolean offer(E e) {
        if (full() && !ensureCapacity(size + 1)) {
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

    protected boolean ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            if (size == Integer.MAX_VALUE) {
                return false;
            }
            grow(minCapacity);
        }
        return true;
    }

    protected void grow(int minCapacity) {
        int newCapacity = newCapacity(minCapacity);
        Object[] newElements = new Object[newCapacity];
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
    }

    protected int newCapacity(int minCapacity) {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity == 0 ? DEFAULT_CAPACITY : oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - Integer.MAX_VALUE > 0) {
            newCapacity = Integer.MAX_VALUE;
        }
        return newCapacity;
    }

}
