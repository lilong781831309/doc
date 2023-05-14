package org.xinhua.example.design_pattern.creational.factory_method.demo2;


import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 18:00
 * @Description: 链表 数组实现       具体工厂
 * @Version: 1.0
 */
public class ArrayList<E> implements Collection<E> {

    private Object[] elements = {};
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int initCapacity) {
        if (initCapacity <= 0) {
            initCapacity = DEFAULT_CAPACITY;
        }
        if (initCapacity > Integer.MAX_VALUE) {
            initCapacity = Integer.MAX_VALUE;
        }
        this.elements = new Object[initCapacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        return (E) elements[index];
    }

    @Override
    public void add(E e) {
        elements[size++] = e;
    }

    @Override
    public void remove(E e) {
        int index = 0;
        while (index < size) {
            if (elements[index].equals(e)) {
                break;
            }
            index++;
        }
        remove(index);
    }

    @Override
    public void remove(int index) {
        if (size > 0 && index >= 0 && index < size) {
            if (index < size - 1) {
                while (index < size - 1) {
                    elements[index] = elements[index + 1];
                    index++;
                }
            }
            elements[--size] = null;
        }
    }

    @Override
    public void addAll(Collection<? extends E> collection) {
        Iterator<E> it = collection.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

    @Override
    public void forEach(Consumer<E> action) {
        for (int i = 0; i < size; i++) {
            action.accept((E) elements[i]);
        }
    }

    // 具体产品
    private class ArrayListIterator implements Iterator<E> {

        int cursor = 0;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            lastRet = cursor++;
            return (E) elements[lastRet];
        }

        @Override
        public void remove() {
            if (lastRet != -1) {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        }
    }
}
