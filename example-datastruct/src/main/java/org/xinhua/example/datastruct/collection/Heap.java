package org.xinhua.example.datastruct.collection;

import java.util.Comparator;

public class Heap<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private final Comparator<E> comparator;
    private Object[] values = {};
    private int size;

    public Heap() {
        this.comparator = null;
    }

    public Heap(int initCapacity) {
        this(initCapacity, null);
    }

    public Heap(Comparator<E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public Heap(int initCapacity, Comparator<E> comparator) {
        if (initCapacity <= 0) {
            initCapacity = DEFAULT_CAPACITY;
        }
        this.values = new Object[initCapacity];
        this.comparator = comparator;
    }

    public Heap(E... items) {
        this(DEFAULT_CAPACITY, null, items);
    }

    public Heap(Comparator<E> comparator, E... items) {
        this(DEFAULT_CAPACITY, null, items);
    }

    public Heap(int initCapacity, Comparator<E> comparator, E... items) {
        this(initCapacity > items.length ? initCapacity : (items.length > DEFAULT_CAPACITY ? items.length : DEFAULT_CAPACITY));
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                throw new RuntimeException("不支持null");
            }
            values[i] = items[i];
        }
        size = items.length;
        heapify();
    }

    public void insert(E e) {
        ensureCapacity(size + 1);
        values[size] = e;
        shiftUp(size++);
    }

    public E peek() {
        if (size == 0) {
            return null;
        }
        return (E) values[size - 1];
    }

    public E extract() {
        if (size == 0) {
            return null;
        }
        E e = (E) values[0];
        values[0] = values[size - 1];
        values[--size] = null;
        shiftDown(0);
        return e;
    }

    public E replace(E e) {
        E value = null;
        if (size == 0) {
            values[0] = e;
            size = 1;
        } else {
            value = (E) values[0];
            values[0] = e;
            shiftDown(0);
        }
        return value;
    }

    public int size() {
        return size;
    }

    public boolean empty() {
        return size == 0;
    }

    private int cmp(Object e1, Object e2) {
        return comparator != null ? comparator.compare((E) e1, (E) e2) : ((Comparable) e1).compareTo(e2);
    }

    private void shiftUp(int k) {
        int p = (k - 1) >> 1;
        E e = (E) values[k];
        while (p >= 0 && cmp(e, values[p]) < 0) {
            values[k] = values[p];
            k = p;
            p = (k - 1) >> 1;
        }
        values[k] = e;
    }

    private void shiftDown(int k) {
        int p = k;
        k = (p << 1) + 1;
        E e = (E) values[p];
        while (k < size) {
            if (k + 1 < size && cmp(values[k + 1], values[k]) < 0) {
                k++;
            }
            if (cmp(e, values[k]) > 0) {
                values[p] = values[k];
                p = k;
                k = (p << 1) + 1;
            } else {
                break;
            }
        }
        values[p] = e;
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
        if (minCapacity > values.length) {
            if (size == Integer.MAX_VALUE) {
                throw new RuntimeException("size 超过最大值 :" + Integer.MAX_VALUE);
            }
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int newCapacity = newCapacity(minCapacity);
        Object[] newValues = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newValues[i] = values[i];
        }
        values = newValues;
    }

    private int newCapacity(int minCapacity) {
        int oldCapacity = values.length;
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
