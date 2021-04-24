package org.xinua.example.datastruct.heap;

import java.util.Comparator;

public class Heap<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int MIN_CAPATICY = 10;
    private static final Object[] EMPTY_VALUES = {};
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
        if (initCapacity > 0) {
            values = new Object[initCapacity];
        } else if (initCapacity == 0) {
            values = EMPTY_VALUES;
        } else {
            throw new RuntimeException("initCapacity 不正确 : " + initCapacity);
        }
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
        if (!empty()) {
            value = (E) values[0];
            values[0] = e;
            shiftDown(0);
        } else {
            values[0] = e;
            size++;
        }
        return value;
    }

    public int size() {
        return size;
    }

    public boolean empty() {
        return size == 0;
    }

    private void swap(int i, int j) {
        Object temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }

    private void shiftUp(int k) {
        if (k == 0) {
            return;
        }
        int p = (k - 1) >>> 1;
        if (comparator != null) {
            while (p >= 0 && comparator.compare((E) values[k], (E) values[p]) < 0) {
                swap(k, p);
                k = p;
                p = (k - 1) >>> 1;
            }
        } else {
            while (p >= 0 && ((Comparable) values[k]).compareTo((Comparable<E>) values[p]) < 0) {
                swap(k, p);
                k = p;
                p = (k - 1) >>> 1;
            }
        }
    }

    private void shiftDown(int k) {
        int p = k;
        int pos = (p << 1) + 1;
        if (comparator != null) {
            while (pos < size) {
                if (pos + 1 < size && comparator.compare((E) values[pos], (E) values[pos + 1]) > 0) {
                    pos++;
                }
                if (comparator.compare((E) values[p], (E) values[pos]) > 0) {
                    swap(p, pos);
                    p = pos;
                    pos = (p << 1) + 1;
                } else {
                    break;
                }
            }
        } else {
            while (pos <= size) {
                if (pos + 1 < size && ((Comparable) values[pos]).compareTo((Comparable<E>) values[pos + 1]) > 0) {
                    pos++;
                }
                if (((Comparable) values[p]).compareTo((Comparable<E>) values[pos]) > 0) {
                    swap(p, pos);
                    p = pos;
                    pos = (p << 1) + 1;
                } else {
                    break;
                }
            }
        }
    }

    private void heapify() {
        if (size > 0) {
            int p = (size - 1) >>> 1;
            while (p >= 0) {
                shiftDown(p--);
            }
        }
    }

    private void grow() {
        if (size == Integer.MAX_VALUE) {
            throw new RuntimeException("size 超过最大值");
        }
        int capacity = values.length;
        if (capacity > Integer.MAX_VALUE >>> 1) {
            capacity = Integer.MAX_VALUE;
        } else if (capacity < MIN_CAPATICY) {
            capacity = MIN_CAPATICY;
        } else {
            capacity = capacity << 1;
        }

        Object[] newValues = new Object[capacity];
        for (int i = 0; i < values.length - 1; i++) {
            newValues[i] = values[i];
        }
        values = newValues;
    }

}
