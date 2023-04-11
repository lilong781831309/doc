package org.xinua.example.datastruct.collection;

/**
 * @Author: lilong
 * @createDate: 2023/4/11 2:20
 * @Description: 栈    数组实现
 * @Version: 1.0
 */
public class ArrayStack<E> implements Stack<E> {

    private int size;
    private Object[] elements = {};

    private static final int DEFAULT_CAPACITY = 16;


    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayStack(int initCapacity) {
        if (initCapacity <= 0) {
            initCapacity = DEFAULT_CAPACITY;
        }
        elements = new Object[initCapacity];
    }

    @Override
    public boolean push(E e) {
        if (full() && !newCapacity()) {
            return false;
        }
        elements[size++] = e;
        return true;
    }

    @Override
    public E pop() {
        if (empty()) {
            return null;
        }
        E e = (E) elements[size - 1];
        elements[--size] = null;
        return e;
    }

    @Override
    public E peek() {
        if (empty()) {
            return null;
        }
        return (E) elements[size - 1];
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

    private boolean newCapacity() {
        if (size == Integer.MAX_VALUE) {
            return false;
        }

        int capacity = size << 1;

        if (capacity - Integer.MAX_VALUE > 0) {
            capacity = Integer.MAX_VALUE;
        }

        Object[] newElements = new Object[capacity];

        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }

        elements = newElements;

        return true;
    }

}
