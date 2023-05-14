package org.xinhua.example.design_pattern.creational.prototype;

import java.io.*;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 18:00
 * @Description: 链表 数组实现
 *               实现 Cloneable，Serializable
 * @Version: 1.0
 */
public class ArrayList<E> implements Cloneable, Serializable {

    private static final long serialVersionUID = 4143064776899151024L;
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

    public int size() {
        return size;
    }

    public E get(int index) {
        return (E) elements[index];
    }

    public void add(E e) {
        grow();
        elements[size++] = e;
    }

    private void grow() {
        if (size == elements.length) {
            int newLen = size * 2;
            if (newLen > Integer.MAX_VALUE) {
                newLen = Integer.MAX_VALUE;
            }
            Object[] newElements = new Object[newLen];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

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

    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elements = Arrays.copyOf(elements, size);
            return v;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Object deepClone() {
        try {
            ArrayList<?> list = (ArrayList<?>) super.clone();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(list);
            byte[] bytes = baos.toByteArray();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        int iMax = size - 1;
        if (iMax == -1)
            return "[]";
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.valueOf(elements[i]));
            if (i == size - 1)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public Iterator<E> iterator() {
        return new Iterator();
    }

    public void forEach(Consumer<E> action) {
        for (int i = 0; i < size; i++) {
            action.accept((E) elements[i]);
        }
    }

    // 具体产品
    public class Iterator<E> {

        int cursor = 0;
        int lastRet = -1;

        public boolean hasNext() {
            return cursor != size;
        }

        public E next() {
            lastRet = cursor++;
            return (E) elements[lastRet];
        }

        public void remove() {
            if (lastRet != -1) {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        }
    }
}
