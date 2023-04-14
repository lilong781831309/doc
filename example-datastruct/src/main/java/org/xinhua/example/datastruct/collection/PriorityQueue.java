package org.xinhua.example.datastruct.collection;

import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/12 1:18
 * @Description: 优先队列   堆实现
 * @Version: 1.0
 */
public class PriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Object[] elements;
    private final Comparator<E> comparator;

    public PriorityQueue() {
        this(DEFAULT_CAPACITY, null);
    }

    public PriorityQueue(int initCapacity) {
        this(initCapacity, null);
    }

    public PriorityQueue(E... items) {
        this(items.length, null);
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                throw new RuntimeException("不支持null");
            }
            elements[i] = items[i];
        }
        size = items.length;
        heapify();
    }

    public PriorityQueue(int initCapacity, Comparator<E> comparator) {
        if (initCapacity <= 0) {
            initCapacity = DEFAULT_CAPACITY;
        }
        this.elements = new Object[initCapacity];
        this.comparator = comparator;
    }

    @Override
    public boolean offer(E e) {
        if (size == elements.length) {
            grow();
        }
        elements[size] = e;
        shiftUp(size++);
        return true;
    }

    @Override
    public E poll() {
        if (empty()) {
            return null;
        }
        E e = (E) elements[0];
        elements[0] = elements[size - 1];
        elements[--size] = null;
        shiftDown(0);
        return e;
    }

    @Override
    public E peek() {
        return empty() ? null : (E) elements[0];
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private void shiftUp(int k) {
        if (k == 0) {
            return;
        }
        int p = (k - 1) >> 1;
        Object e = elements[k];
        if (comparator != null) {
            while (p >= 0 && comparator.compare((E) e, (E) elements[p]) < 0) {
                elements[k] = elements[p];
                k = p;
                p = (k - 1) >> 1;
            }
        } else {
            while (p >= 0 && ((Comparable) e).compareTo((Comparable<E>) elements[p]) < 0) {
                elements[k] = elements[p];
                k = p;
                p = (k - 1) >> 1;
            }
        }
        elements[k] = e;
    }

    private void shiftDown(int k) {
        int p = k;
        int pos = (p << 1) + 1;
        Object e = elements[p];
        if (comparator != null) {
            while (pos < size) {
                if (pos + 1 < size && comparator.compare((E) elements[pos], (E) elements[pos + 1]) > 0) {
                    pos++;
                }
                if (comparator.compare((E) e, (E) elements[pos]) > 0) {
                    elements[p] = elements[pos];
                    p = pos;
                    pos = (p << 1) + 1;
                } else {
                    break;
                }
            }
        } else {
            while (pos < size) {
                if (pos + 1 < size && ((Comparable) elements[pos]).compareTo((Comparable<E>) elements[pos + 1]) > 0) {
                    pos++;
                }
                if (((Comparable) e).compareTo((Comparable<E>) elements[pos]) > 0) {
                    elements[p] = elements[pos];
                    p = pos;
                    pos = (p << 1) + 1;
                } else {
                    break;
                }
            }
        }
        elements[p] = e;
    }

    private void heapify() {
        if (size > 0) {
            int p = (size - 1) >> 1;
            while (p >= 0) {
                shiftDown(p--);
            }
        }
    }

    private void grow() {
        if (size == Integer.MAX_VALUE) {
            throw new RuntimeException("size 超过最大值 :" + Integer.MAX_VALUE);
        }
        int capacity = elements.length + (elements.length >> 1);
        if (capacity - Integer.MAX_VALUE > 0) {
            capacity = Integer.MAX_VALUE;
        }
        Object[] newElements = new Object[capacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }
}
