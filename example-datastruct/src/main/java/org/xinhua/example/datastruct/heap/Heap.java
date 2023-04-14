package org.xinhua.example.datastruct.heap;

import java.util.Comparator;

public class Heap<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private final Comparator<E> comparator;
    private Object[] values;
    private int size;

    public Heap() {
        this(DEFAULT_CAPACITY, null);
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
        this(null, items);
    }

    public Heap(Comparator<E> comparator, E... items) {
        this(items.length, comparator);
        for (int i = 0; i < items.length; i++) {
            values[i] = items[i];
        }
        size = items.length;
        heapify();
    }

    public void insert(E e) {
        if (size == values.length) {
            grow();
        }
        values[size] = e;
        shiftUp(size);
        size++;
    }

    public E peek() {
        return empty() ? null : (E) values[0];
    }

    public E extract() {
        if (empty()) {
            return null;
        }
        E e = (E) values[0];
        values[0] = values[size - 1];
        values[size--] = null;
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

    private void shiftUp(int k) {
        if (k == 0) {
            return;
        }
        int p = (k - 1) >> 1;
        Object e = values[k];
        if (comparator != null) {
            while (p >= 0 && comparator.compare((E) e, (E) values[p]) < 0) {
                values[k] = values[p];
                k = p;
                p = (k - 1) >> 1;
            }
        } else {
            while (p >= 0 && ((Comparable) e).compareTo((Comparable<E>) values[p]) < 0) {
                values[k] = values[p];
                k = p;
                p = (k - 1) >> 1;
            }
        }
        values[k] = e;
    }

    private void shiftDown(int k) {
        int p = k;
        int pos = (p << 1) + 1;
        Object e = values[p];
        if (comparator != null) {
            while (pos < size) {
                if (pos + 1 < size && comparator.compare((E) values[pos], (E) values[pos + 1]) > 0) {
                    pos++;
                }
                if (comparator.compare((E) e, (E) values[pos]) > 0) {
                    values[p] = values[pos];
                    p = pos;
                    pos = (p << 1) + 1;
                } else {
                    break;
                }
            }
        } else {
            while (pos < size) {
                if (pos + 1 < size && ((Comparable) values[pos]).compareTo((Comparable<E>) values[pos + 1]) > 0) {
                    pos++;
                }
                if (((Comparable) e).compareTo((Comparable<E>) values[pos]) > 0) {
                    values[p] = values[pos];
                    p = pos;
                    pos = (p << 1) + 1;
                } else {
                    break;
                }
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

    private void grow() {
        if (size == Integer.MAX_VALUE) {
            throw new RuntimeException("size 超过最大值 :" + Integer.MAX_VALUE);
        }
        int capacity = values.length + (values.length >> 1);
        if (capacity - Integer.MAX_VALUE > 0) {
            capacity = Integer.MAX_VALUE;
        }

        Object[] newValues = new Object[capacity];
        for (int i = 0; i < values.length - 1; i++) {
            newValues[i] = values[i];
        }
        values = newValues;
    }

}
