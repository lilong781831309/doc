package org.xinhua.example.datastruct.collection;


import java.util.Comparator;

/**
 * @Author: lilong
 * @createDate: 2023/4/12 1:18
 * @Description: 优先队列   堆实现
 * @Version: 1.0
 */
public class PriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] elements = {};
    private final Comparator<E> comparator;

    public PriorityQueue() {
        this.comparator = null;
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
        ensureCapacity(size + 1);
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

    private int cmp(Object e1, Object e2) {
        return comparator != null ? comparator.compare((E) e1, (E) e2) : ((Comparable) e1).compareTo(e2);
    }

    private void shiftUp(int k) {
        if (k == 0) {
            return;
        }
        int p = (k - 1) >> 1;
        Object e = elements[k];
        while (p >= 0 && cmp(e, elements[p]) < 0) {
            elements[k] = elements[p];
            k = p;
            p = (k - 1) >> 1;
        }
        elements[k] = e;
    }

    private void shiftDown(int k) {
        int p = k;
        int pos = (p << 1) + 1;
        Object e = elements[p];
        while (pos < size) {
            if (pos + 1 < size && cmp(elements[pos], elements[pos + 1]) > 0) {
                pos++;
            }
            if (cmp(e, elements[pos]) > 0) {
                elements[p] = elements[pos];
                p = pos;
                pos = (p << 1) + 1;
            } else {
                break;
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

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            if (size == Integer.MAX_VALUE) {
                throw new RuntimeException("size 超过最大值 :" + Integer.MAX_VALUE);
            }
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int newCapacity = newCapacity(minCapacity);
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    private int newCapacity(int minCapacity) {
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
