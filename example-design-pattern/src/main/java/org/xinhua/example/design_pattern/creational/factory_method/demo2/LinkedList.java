package org.xinhua.example.design_pattern.creational.factory_method.demo2;

import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/7 18:00
 * @Description: 链表 链表实现    具体工厂
 * @Version: 1.0
 */
public class LinkedList<E> implements Collection<E> {

    private int size = 0;
    private Node<E> first;
    private Node<E> last;

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        Node<E> node = find(index);
        return node.e;
    }

    @Override
    public void add(E e) {
        Node<E> node = new Node<>();
        node.e = e;
        addLast(node);
    }

    @Override
    public void remove(E e) {
        Node node = first;
        while (node != null && !node.e.equals(e)) {
            node = node.next;
        }
        remove(node);
    }

    @Override
    public void remove(int index) {
        Node<? extends E> node = find(index);
        remove(node);
    }

    private Node<E> find(int index) {
        Node<E> node = null;
        if (index >= 0 && index < size) {
            node = first;
            int n = 0;
            while (n < index) {
                node = node.next;
                n++;
            }
        }
        return node;
    }

    private void addLast(Node node) {
        if (last == null) {
            first = last = node;
        } else {
            if (first == last) {
                first.next = node;
                node.prev = first;
                last = node;
            } else {
                last.next = node;
                node.prev = last;
                last = node;
            }
        }
        size++;
    }

    private void remove(Node node) {
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                first = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                last = node.prev;
            }
            node.prev = null;
            node.next = null;
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
        return new LinkedListIterator();
    }

    @Override
    public void forEach(Consumer<E> action) {
        Node<? extends E> node = first;
        while (node != null) {
            action.accept(node.e);
            node = node.next;
        }
    }

    private class Node<E> {
        Node<E> prev;
        Node<E> next;
        E e;
    }

    // 具体产品
    private class LinkedListIterator implements Iterator<E> {

        Node<E> cursor = LinkedList.this.first;
        Node<E> lastRet = null;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            lastRet = cursor;
            cursor = cursor.next;
            return lastRet.e;
        }

        @Override
        public void remove() {
            if (lastRet != null) {
                LinkedList.this.remove(lastRet);
                lastRet = null;
            }
        }
    }
}
