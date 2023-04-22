
package org.xinhua.example.datastruct.collection;


/**
 * @Author: lilong
 * @createDate: 2023/4/21 5:33
 * @Description: 动态数组
 * @Version: 1.0
 */

public class ArrayList<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] elements = {};

    public ArrayList() {
    }

    public ArrayList(int initCapacity) {
        if (initCapacity > 0) {
            elements = new Object[initCapacity];
        }
    }

    @Override
    public E get(int index) {
        return (E) elements[index];
    }

    @Override
    public E set(int index, E e) {
        E value = (E) elements[index];
        elements[index] = e;
        return value;
    }

    @Override
    public void add(E e) {
        add(size, e);
    }

    @Override
    public void add(int index, E e) {
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = e;
        size++;
    }

    @Override
    public void remove(E e) {
        int index = indexOf(e);
        if (index > 0) {
            removeAt(index);
        }
    }

    @Override
    public E removeAt(int index) {
        E value = (E) elements[index];
        for (int i = index + 1; i < size; i++) {
            elements[i - 1] = elements[i];
        }
        size--;
        elements[size] = null;
        return value;
    }

    @Override
    public void removeBetween(int start, int end) {
        int s = start, e = end + 1, c = 0;
        while (s <= end && e < size) {
            elements[s] = elements[e];
            s++;
            e++;
        }
        while (e < size) {
            elements[s] = elements[e];
            s++;
            e++;
        }
        while (s < size) {
            elements[s] = null;
            s++;
            c++;
        }
        size -= c;
    }

    @Override
    public int indexOf(E e) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            if (size == Integer.MAX_VALUE) {
                throw new RuntimeException("size 超过最大值 :" + Integer.MAX_VALUE);
            }
            grow(size + 1);
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

